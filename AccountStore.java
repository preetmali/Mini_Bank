import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccountStore {

    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public synchronized void add(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "Account number already exists.");
        }

        accounts.put(account.getAccountNumber(), account);
    }

    public synchronized Account get(String accountNumber)
            throws BankException {

        Account account = accounts.get(accountNumber);

        if (account == null) {
            throw new BankException("Account not found.");
        }

        return account;
    }

    public synchronized boolean delete(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }

    public synchronized ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized ArrayList<Account> sortedByBalance() {
        ArrayList<Account> result = getAll();
        Collections.sort(result);
        return result;
    }

    public synchronized void replaceAll(
            ArrayList<Account> loadedAccounts) {

        accounts.clear();

        for (Account account : loadedAccounts) {
            add(account);
        }
    }
}