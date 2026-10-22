public class SavingsAccount extends Account {

    public SavingsAccount(String name, double balance)
            throws InvalidAmountException {
        super(name, balance);
    }

    public SavingsAccount(String id, String name, double balance)
            throws InvalidAmountException {
        super(id, name, balance);
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return amount <= getBalance();
    }
}