public class CommandParser {

    public static Command parse(int choice) {
        MenuOption option = switch (choice) {
            case 1 -> MenuOption.OPEN_ACCOUNT;
            case 2 -> MenuOption.DEPOSIT;
            case 3 -> MenuOption.WITHDRAW;
            case 4 -> MenuOption.TRANSFER;
            case 5 -> MenuOption.LIST_ACCOUNTS;
            case 6 -> MenuOption.ACCOUNT_INFORMATION;
            case 7 -> MenuOption.DELETE_ACCOUNT;
            case 8 -> MenuOption.EXIT;
            default -> null;
        };

        return option == null ? null : new Command(option);
    }
}