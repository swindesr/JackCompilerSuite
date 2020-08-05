package compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
    private BufferedWriter writer;
    private String className;

    public VMWriter(String fileName, String className) {
        this.className = className;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public void writePush(String seg, int index) {
        if (seg.equals("field")) seg = "this";
        write("push " + seg + " " + index);
    }

    public void writePop(String seg, int index) {
        if (seg.equals("field")) seg = "this";
        write("pop " + seg + " " + index);
    }

    public void writeArithmetic(String cmd) {
        String toWrite;
        switch (cmd) {
            case "+":       toWrite = "add";
                            break;
            case "-":       toWrite = "sub";
                            break;
            case "*":       toWrite = "call Math.multiply 2";
                            break;
            case "/":       toWrite = "call Math.divide 2";
                            break;
            case "~":       toWrite = "neg";
                            break;
            case "=":       toWrite = "eq";
                            break;
            case "&lt;":    toWrite = "lt";
                            break;
            case "&gt;":    toWrite = "gt";
                            break;
            case "&amp;":   toWrite = "and";
                            break;
            case "|":       toWrite = "or";
                            break;
            case "!":       toWrite = "not";
                            break;
            default:        toWrite = "error";
        }
        write(toWrite);
    }

    public void writeLabel(String label) {
        write("label " + label);
    }

    public void writeGoto(String label) {
        write("goto " + label);
    }

    public void writeIf(String label) {
        write("if-goto " + label);
    }

    public void writeCall(String name, int nArgs) {
        write("call " + name + " " + nArgs);
    }

    public void writeFunction(String name, int nLocals) {
        write("function " + className + "." + name + " " + nLocals);
    }

    public void writeReturn() {
        write("return");
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String s) {
        try {
            writer.write(s);
            writer.newLine();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
