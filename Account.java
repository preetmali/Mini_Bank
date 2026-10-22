import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Account implements Comparable<Account> {

    private static final AtomicInteger NEXT_NUMBER = new AtomicInteger(1001);

    @Id
    private final String accountNumber;

    private final String customerName;
    protected double balance;

    protected Account(String customerName, double openingBalance)
            throws InvalidAmountException {

        this("AC" + NEXT_NUMBER.getAndIncrement(),
                customerName, openingBalance);
    }

    protected Account(String accountNumber, String customerName,
            double openingBalance)
            throws InvalidAmountException {

        if (!Double.isFinite(openingBalance) || openingBalance < 0) {
            throw new InvalidAmountException(
                    "Opening balance cannot be negative.");
        }

        this.accountNumber = Objects.requireNonNull(accountNumber);
        this.customerName = Validator.requireText(customerName, "Customer name");
        this.balance = openingBalance;

        updateNextNumber(accountNumber);
    }

    public static void updateNextNumber(String accountNumber) {

        if (accountNumber == null || !accountNumber.startsWith("AC")) {
            return;
        }

        try {
            int number = Integer.parseInt(accountNumber.substring(2));

            if (number < 1001) {
                return;
            }

            NEXT_NUMBER.updateAndGet(
                    current -> Math.max(current, number + 1));

        } catch (NumberFormatException e) {
            // Ignore account numbers that do not follow AC<number>.
        }
    }

    public abstract double interestRate();

    protected abstract boolean canWithdraw(double amount);

    public synchronized void deposit(double amount)
            throws InvalidAmountException {

        Validator.requireAmount(amount);

        if (!Double.isFinite(balance + amount)) {
            throw new InvalidAmountException(
                    "Balance limit exceeded.");
        }

        balance += amount;
    }

    public synchronized void withdraw(double amount)
            throws BankException {

        Validator.requireAmount(amount);

        if (!canWithdraw(amount)) {
            throw new InsufficientBalanceException(
                    "Insufficient balance or account rules do not allow withdrawal.");
        }

        balance -= amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return getClass().getSimpleName();
    }

    @Override
    public int compareTo(Account other) {
        return Double.compare(
                other.getBalance(),
                this.getBalance());
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + " | Name: " + customerName
                + " | Type: " + getAccountType()
                + " | Balance: " + String.format("%.2f", getBalance())
                + " | Interest: " + interestRate() + "%";
    }

}
