package commands;

public class C_Function extends Command {
    int arg2;
    String fileName;

    public C_Function(String arg1, int arg2, int id, String fileName) {
        super(arg1, id);
        this.arg2 = arg2;
        this.fileName = fileName;
    }

    @Override
    public String translate() {
        StringBuilder code = new StringBuilder();

        code.append("(").append(arg1).append(")\n");

        for (int i = 0; i < arg2; i ++) {
            if (i == 0) {
                code.append("@0\n")
                        .append("D=A;\n")
                        .append("@LCL\n")
                        .append("A=M;\n")
                        .append("M=D;\n");
            } else {
                code.append("A=A+1;\n")
                        .append("M=D;\n");
            }
        }

        for (int i = 0; i < arg2; i ++) {
            if (i == 0)
                code.append("@SP\n");
            code.append("M=M+1;\n");
        }

        return code.toString();
    }
}
