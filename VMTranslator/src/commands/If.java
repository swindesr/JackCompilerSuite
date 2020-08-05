package commands;

public class If extends Command {
    String fileName;

    public If (String arg1, int id, String fileName) {
        super(arg1, id);
        this.fileName = fileName;
    }

    @Override
    public String translate() {
        return "@SP\n" +
                "AM=M-1;\n" +
                "D=M;\n" +
                "@" + fileName + "$" + arg1 + "\n" +
                "D;JNE\n";
    }
}
