public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String name, double balance)
            throws InvalidAmountException {
        super(name, balance);
    }

    public FixedDepositAccount(String id, String name, double balance)
            throws InvalidAmountException {
        super(id, name, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    protected boolean canWithdraw(double amount) {
        return false;
    }
}