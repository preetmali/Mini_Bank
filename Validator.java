public class Validator {

    public static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }

        return value.trim();
    }

    public static double requireAmount(double amount)
            throws InvalidAmountException {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero.");
        }

        return amount;
    }
}