package commands;

public class Label extends Command {
    String fileName;

    public Label (String arg1, int id, String fileName) {
        super(arg1, id);
        this.fileName = fileName;
    }

    @Override
    public String translate() { return "(" + fileName + "$" + arg1 + ")\n"; }
}
