import java.util.ArrayList;

public class TransactionProcessor implements AutoCloseable {

    private final AccountStore store;

    public TransactionProcessor(AccountStore store) {
        this.store = store;
    }

    public void register(Account account) {
        store.add(account);
    }

    public Account get(String id) throws BankException {
        return store.get(id);
    }

    public void deposit(String id, double amount)
            throws BankException {

        store.get(id).deposit(amount);
    }

    public void withdraw(String id, double amount)
            throws BankException {

        store.get(id).withdraw(amount);
    }

    public synchronized void transfer(
            String from, String to, double amount)
            throws BankException {

        if (from.equals(to)) {
            throw new BankException(
                    "Source and destination accounts must be different.");
        }

        Account source = store.get(from);
        Account destination = store.get(to);

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be greater than zero.");
        }

        if (amount > source.getBalance()) {
            throw new InsufficientBalanceException(
                    "Insufficient balance.");
        }

        source.withdraw(amount);

        try {
            destination.deposit(amount);
        } catch (BankException e) {
            source.deposit(amount);
            throw e;
        }
    }

    public boolean delete(String id) {
        return store.delete(id);
    }

    public ArrayList<Account> getAll() {
        return store.getAll();
    }

    public void replaceAll(ArrayList<Account> accounts) {
        store.replaceAll(accounts);
    }

    @Override
    public void close() {
        // No external resources to close.
    }
}