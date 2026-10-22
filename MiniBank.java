import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class MiniBank {

    private static final BankInfo BANK = new BankInfo("MiniBank", "Charusat");

    private static final Path DATA_FILE = Path.of("data", "accounts.txt");

    public static void main(String[] args) {

        AccountStore store = new AccountStore();
        PersistenceService persistence = new PersistenceService();

        try (Scanner sc = new Scanner(System.in);
                TransactionProcessor processor = new TransactionProcessor(store)) {

            loadAtStartup(store, persistence);

            boolean running = true;

            while (running) {
                printMenu();

                int choice = readInt(sc, "Choose option: ");
                Command command = CommandParser.parse(choice);

                if (command == null) {
                    System.out.println("Invalid choice. Enter 1 to 8.");
                    continue;
                }

                try {
                    switch (command.getOption()) {

                        case OPEN_ACCOUNT -> {
                            openAccount(sc, processor);
                            save(processor, persistence);
                        }

                        case DEPOSIT -> {
                            doDeposit(sc, processor);
                            save(processor, persistence);
                        }

                        case WITHDRAW -> {
                            doWithdraw(sc, processor);
                            save(processor, persistence);
                        }

                        case TRANSFER -> {
                            doTransfer(sc, processor);
                            save(processor, persistence);
                        }

                        case LIST_ACCOUNTS ->
                            listAccounts(store);

                        case ACCOUNT_INFORMATION ->
                            showAccountInformation(sc, processor);

                        case DELETE_ACCOUNT -> {
                            deleteAccount(sc, processor);
                            save(processor, persistence);
                        }

                        case EXIT -> {
                            save(processor, persistence);
                            running = false;
                            System.out.println(
                                    "Thank you for using MiniBank.!");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("MiniBank could not start: "
                    + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\n===== MiniBank =====");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. List Accounts");
        System.out.println("6. Account Information");
        System.out.println("7. Delete Account");
        System.out.println("8. Exit");
    }

    private static void loadAtStartup(
            AccountStore store, PersistenceService persistence)
            throws IOException, InvalidAmountException {

        ArrayList<Account> accounts = persistence.loadAccounts(DATA_FILE);

        store.replaceAll(accounts);

        System.out.println(BANK);
        System.out.println("Loaded " + accounts.size()
                + " accounts from " + DATA_FILE + ".");
    }

    private static void save(
            TransactionProcessor processor,
            PersistenceService persistence) throws IOException {

        persistence.saveAccounts(processor.getAll(), DATA_FILE);
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static double readAmount(Scanner sc)
            throws InvalidAmountException {

        System.out.print("Amount: ");

        try {
            double amount = Double.parseDouble(sc.nextLine().trim());
            return Validator.requireAmount(amount);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Enter a valid amount.");
        }
    }

    private static void openAccount(
            Scanner sc, TransactionProcessor processor)
            throws InvalidAmountException {

        System.out.print("Customer name: ");
        String name = sc.nextLine().trim();

        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Fixed Deposit Account");

        int type = readInt(sc, "Choose account type: ");
        double openingBalance = readAmount(sc);

        Account account = switch (type) {
            case 1 -> new SavingsAccount(name, openingBalance);
            case 2 -> new CurrentAccount(name, openingBalance);
            case 3 -> new FixedDepositAccount(name, openingBalance);
            default -> throw new IllegalArgumentException(
                    "Invalid account type.");
        };

        processor.register(account);

        System.out.println("Account created successfully.");
        System.out.println(account);
    }

    private static void doDeposit(
            Scanner sc, TransactionProcessor processor)
            throws BankException {

        System.out.print("Account number: ");
        String id = sc.nextLine().trim();

        double amount = readAmount(sc);
        processor.deposit(id, amount);

        System.out.println("Deposit successful.");
        System.out.println("New balance: "
                + processor.get(id).getBalance());
    }

    private static void doWithdraw(
            Scanner sc, TransactionProcessor processor)
            throws BankException {

        System.out.print("Account number: ");
        String id = sc.nextLine().trim();

        double amount = readAmount(sc);
        processor.withdraw(id, amount);

        System.out.println("Withdrawal successful.");
        System.out.println("New balance: "
                + processor.get(id).getBalance());
    }

    private static void doTransfer(
            Scanner sc, TransactionProcessor processor)
            throws BankException {

        System.out.print("From account: ");
        String from = sc.nextLine().trim();

        System.out.print("To account: ");
        String to = sc.nextLine().trim();

        double amount = readAmount(sc);
        processor.transfer(from, to, amount);

        System.out.println("Transfer successful.");
    }

    private static void listAccounts(AccountStore store) {
        ArrayList<Account> accounts = store.sortedByBalance();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    private static void showAccountInformation(
            Scanner sc, TransactionProcessor processor)
            throws BankException {

        System.out.print("Account number: ");
        String id = sc.nextLine().trim();

        Account account = processor.get(id);

        System.out.println(StatementFormatter.format(account));
    }

    private static void deleteAccount(
            Scanner sc, TransactionProcessor processor)
            throws BankException {

        System.out.print("Account number: ");
        String id = sc.nextLine().trim();

        Account account = processor.get(id);

        System.out.println(account);
        System.out.print("Delete this account? (yes/no): ");

        String confirmation = sc.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            processor.delete(id);
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }
}