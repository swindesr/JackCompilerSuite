package commands;

public class Call extends Command {
    int arg2;
    String fileName;

    public Call(String arg1, int arg2, int id, String fileName) {
        super(arg1, id);
        this.arg2 = arg2;
        this.fileName = fileName;
    }

    @Override
    public String translate() {
        int argOffset = 5 + arg2;

        // single string concatenation instead of .append() chain used per IntelliJ performance recommendation
        return "@" + arg1 + "$ret." + id + "\n" +
                "D=A;\n" +
                "@SP\n" +
                "M=M+1;\n" +
                "A=M-1;\n" +
                "M=D;\n" +
                "@LCL\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M+1;\n" +
                "A=M-1;\n" +
                "M=D;\n" +
                "@ARG\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M+1;\n" +
                "A=M-1;\n" +
                "M=D;\n" +
                "@THIS\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M+1;\n" +
                "A=M-1;\n" +
                "M=D;\n" +
                "@THAT\n" +
                "D=M\n" +
                "@SP\n" +
                "M=M+1;\n" +
                "A=M-1;\n" +
                "M=D;\n" +
                "@SP\n" +
                "D=M;\n" +
                "@" + argOffset + "\n" +
                "D=D-A;\n" +
                "@ARG\n" +
                "M=D;\n" +
                "@SP\n" +
                "D=M;\n" +
                "@LCL\n" +
                "M=D;\n" +
                "@" + arg1 + "\n" +
                "0;JMP\n" +
                "(" + arg1 + "$ret." + id + ")\n";
    }
}
