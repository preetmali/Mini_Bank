public class CurrentAccount extends Account {

    public CurrentAccount(String name, double balance)
            throws InvalidAmountException {
        super(name, balance);
    }

    public CurrentAccount(String id, String name, double balance)
            throws InvalidAmountException {
        super(id, name, balance);
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return amount <= getBalance();
    }
}