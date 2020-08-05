package commands;

public abstract class Command {
    String arg1;
    int id;

    public Command(String arg1, int id) {
        this.arg1 = arg1;
        this.id = id;
    }

    public abstract String translate();
}
