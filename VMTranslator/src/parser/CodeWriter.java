package parser;

import commands.Call;

public class CodeWriter {
    String fileName;

    public CodeWriter(String fileName) {
        this.fileName = fileName;
    }

    public CodeWriter(){}

    public String translate() {
        StringBuilder code = new StringBuilder();
        Parser p = new Parser(fileName);
        p.parse();
        p.getCommands().forEach(command -> code.append(command.translate()));
        return code.toString();
    }

    public String writeStartupCode() {
        return "@256\n" +
                "D=A;\n" +
                "@0\n" +
                "M=D;\n" +
                new Call("Sys.init", 0, -1, "Sys.vm").translate();
    }
}
