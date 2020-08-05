package commands;

public class Push extends Command {
    int arg2;
    String fileName;

    public Push(String arg1, int arg2, int id, String fileName) {
        super(arg1, id);
        this.arg2 = arg2;
        this.fileName = fileName;
    }

    @Override
    public String translate() {
        StringBuilder code = new StringBuilder();

        switch(arg1) {
            case "local":
            case "argument":
            case "this":
            case "that":
                translateLATT(code);
                break;
            case "constant":
                translateConstant(code);
                break;
            case "static":
                translateStatic(code);
                break;
            case "temp":
                translateTemp(code);
                break;
            case "pointer":
                translatePointer(code);
                break;
            default:
        }

        return code.toString();
    }

    private void translateLATT(StringBuilder code) {
        String segmentPointer = getPointer();
        code.append("@").append(arg2).append("\n")
                .append("D=A\n")
                .append("@").append(segmentPointer).append("\n")
                .append("D=D+M;\n")
                .append("@SP\n")
                .append("D=D+M\n")
                .append("A=D-M;\n")
                .append("D=D-A;\n")
                .append("D=D+M;\n")
                .append("A=D-M;\n")
                .append("M=D-A;\n")
                .append("@SP\n")
                .append("M=M+1;\n");
    }

    private String getPointer() {
        switch(arg1) {
            case "local": return "LCL";
            case "argument": return "ARG";
            case "this": return "THIS";
            case "that": return "THAT";
            case "constant":
            case "static":
            case "temp":
            case "pointer":
            default:
        }
        return null;
    }

    private void translateConstant(StringBuilder code) {
        code.append("@").append(arg2).append("\n")
                .append("D=A;\n")
                .append("@SP\n")
                .append("M=M+1;\n")
                .append("A=M-1;\n")
                .append("M=D;\n");
    }

    private void translateStatic(StringBuilder code) {
        code.append("@").append(fileName).append(".").append(arg2).append("\n")
                .append("D=M;\n")
                .append("@SP\n")
                .append("M=M+1;\n")
                .append("A=M-1;\n")
                .append("M=D;\n");
    }

    private void translateTemp(StringBuilder code) {
        code.append("@").append(5 + arg2).append("\n")
                .append("D=M;\n")
                .append("@SP\n")
                .append("M=M+1;\n")
                .append("A=M-1;\n")
                .append("M=D;\n");
    }

    private void translatePointer(StringBuilder code) {
        String segmentPointer = arg2 == 0 ? "THIS" : "THAT";
        code.append("@").append(segmentPointer).append("\n")
                .append("D=M;\n")
                .append("@SP\n")
                .append("M=M+1;\n")
                .append("A=M-1;\n")
                .append("M=D;\n");
    }
}
