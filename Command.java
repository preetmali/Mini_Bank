public class Command {

    private final MenuOption option;

    public Command(MenuOption option) {
        this.option = option;
    }

    public MenuOption getOption() {
        return option;
    }
}