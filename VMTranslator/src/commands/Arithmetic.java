package commands;

public class Arithmetic extends Command {

    public Arithmetic(String arg1, int id) {
        super(arg1, id);
    }

    @Override
    public String translate() {
        StringBuilder code = new StringBuilder();
        switch(arg1) {
            case "add":
            case "sub":
                translateAddSub(code);
                break;
            case "neg":
            case "not":
                translateNegNot(code);
                break;
            case "and":
            case "or":
                translateAndOr(code);
                break;
            case "eq":
            case "gt":
            case "lt":
                translateEqGtLt(code);
                break;
            default:
        }

        return code.toString();
    }

    private void translateAddSub(StringBuilder code) {
        String operator = arg1.equals("add") ? "+" : "-";
        code.append("@SP\n")
                .append("AM=M-1;\n")
                .append("D=M;\n")
                .append("A=A-1;\n")
                .append("M=M").append(operator).append("D;\n");
    }

    private void translateNegNot(StringBuilder code) {
        String operator = arg1.equals("neg") ? "-" : "!";
        code.append("@SP\n")
                .append("A=M-1;\n")
                .append("M=").append(operator).append("M;\n");
    }

    private void translateAndOr(StringBuilder code) {
        String operator = arg1.equals("and") ? "&" : "|";
        code.append("@SP\n")
                .append("AM=M-1;\n")
                .append("D=M;\n")
                .append("A=A-1;\n")
                .append("M=D").append(operator).append("M;\n");
    }

    private void translateEqGtLt(StringBuilder code) {
        String operator;
        if (arg1.equals("eq")) {
            operator = "JEQ";
        } else if (arg1.equals("gt")) {
            operator = "JGT";
        } else {
            operator = "JLT";
        }
        code.append("@SP\n")
                .append("AM=M-1;\n")
                .append("D=M;\n")
                .append("A=A-1;\n")
                .append("D=M-D;\n")
                .append("@IF_TRUE").append(id).append("\n")
                .append("D;").append(operator).append("\n")
                .append("@SP\n")
                .append("A=M-1;\n")
                .append("M=0;\n")
                .append("@IF_END").append(id).append("\n")
                .append("0;JMP\n")
                .append("(IF_TRUE").append(id).append(")\n")
                .append("@SP\n")
                .append("A=M-1;\n")
                .append("M=-1;\n")
                .append("(IF_END").append(id).append(")\n");
    }
}
