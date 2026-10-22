public class StatementFormatter {

    public static String format(Account account) {
        return "\n----- Account Information -----\n"
                + "Account Number: "
                + account.getAccountNumber() + "\n"
                + "Customer Name: "
                + account.getCustomerName() + "\n"
                + "Account Type: "
                + account.getAccountType() + "\n"
                + "Balance: "
                + String.format("%.2f", account.getBalance()) + "\n"
                + "Interest Rate: "
                + account.interestRate() + "%\n"
                + "-------------------------------";
    }
}