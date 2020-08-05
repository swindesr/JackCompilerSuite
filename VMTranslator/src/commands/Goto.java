package commands;

public class Goto extends Command {
    String fileName;

    public Goto (String arg1, int id, String fileName) {
        super(arg1, id);
        this.fileName = fileName;
    }

    @Override
    public String translate() {
        return ("@" + fileName + "$" + arg1 + "\n" +
                "0;JMP\n");
    }
}
