import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    public void saveAccounts(
            ArrayList<Account> accounts, Path file)
            throws IOException {

        Path parent = file.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Path temporary = file.resolveSibling(
                file.getFileName().toString() + ".tmp");

        List<String> lines = new ArrayList<>();

        for (Account account : accounts) {
            lines.add(
                    account.getAccountNumber() + "|"
                            + account.getCustomerName() + "|"
                            + account.getAccountType() + "|"
                            + account.getBalance());
        }

        Files.write(
                temporary,
                lines,
                StandardCharsets.UTF_8);

        try {
            Files.move(
                    temporary,
                    file,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            Files.move(
                    temporary,
                    file,
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public ArrayList<Account> loadAccounts(Path file)
            throws IOException, InvalidAmountException {

        ArrayList<Account> accounts = new ArrayList<>();

        if (!Files.exists(file)) {
            return accounts;
        }

        List<String> lines = Files.readAllLines(
                file, StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);

            if (parts.length != 4) {
                throw new IOException(
                        "Invalid account data at line " + (i + 1));
            }

            String id = parts[0].trim();
            String name = parts[1].trim();
            String type = parts[2].trim();

            double balance;

            try {
                balance = Double.parseDouble(parts[3].trim());
            } catch (NumberFormatException e) {
                throw new IOException(
                        "Invalid balance at line " + (i + 1), e);
            }

            if (id.isEmpty() || name.isEmpty()) {
                throw new IOException(
                        "Account number or customer name is empty at line "
                                + (i + 1));
            }

            Account account = switch (type) {

                case "SavingsAccount" ->
                    new SavingsAccount(id, name, balance);

                case "CurrentAccount" ->
                    new CurrentAccount(id, name, balance);

                case "FixedDepositAccount" ->
                    new FixedDepositAccount(id, name, balance);

                default -> throw new IOException(
                        "Unknown account type at line " + (i + 1));
            };

            Account.updateNextNumber(id);

            accounts.add(account);
        }

        return accounts;
    }

}
