package commands;

public class Return extends Command {

    public Return(String arg1, int id) {
        super(arg1, id);
    }

    @Override
    public String translate() {
        // single string concatenation instead of .append() chain used per IntelliJ performance recommendation
        return "@LCL\n" +
                "D=M\n" +
                "@R13\n" +
                "M=D;\n" +
                "@5\n" +
                "D=A;\n" +
                "@R13\n" +
                "A=M-D;\n" +
                "D=M;\n" +
                "@R14\n" +
                "M=D;\n" +
                "@SP\n" +
                "AM=M-1;\n" +
                "D=M;\n" +
                "@ARG\n" +
                "A=M\n" +
                "M=D;\n" +
                "D=A+1;\n" +
                "@SP\n" +
                "M=D;\n" +
                "@4\n" +
                "D=A;\n" +
                "@R13\n" +
                "A=M-D;\n" +
                "D=M;\n" +
                "@LCL\n" +
                "M=D;\n" +
                "@3\n" +
                "D=A;\n" +
                "@R13\n" +
                "A=M-D;\n" +
                "D=M;\n" +
                "@ARG\n" +
                "M=D;\n" +
                "@2\n" +
                "D=A;\n" +
                "@R13\n" +
                "A=M-D;\n" +
                "D=M;\n" +
                "@THIS\n" +
                "M=D;\n" +
                "@R13\n" +
                "A=M-1;\n" +
                "D=M;\n" +
                "@THAT\n" +
                "M=D;\n" +
                "@R14\n" +
                "A=M;\n" +
                "0;JMP\n";
    }
}
