public class BankInfo {

    private String bankName;
    private String branch;

    public BankInfo(String bankName, String branch) {
        this.bankName = bankName;
        this.branch = branch;
    }

    @Override
    public String toString() {
        return bankName + " | Branch: " + branch;
    }
}