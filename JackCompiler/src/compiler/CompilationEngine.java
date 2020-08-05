package compiler;

import compiler.symbols.SymbolTable;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.LinkedList;
import java.util.List;

public class CompilationEngine {
    private LinkedList<Token> tokens;
    private List<Token> compiledTokens;
    private SymbolTable symbolTable;
    private VMWriter writer;
    private String className;
    private int whileCount, ifCount = 0;

    public CompilationEngine(LinkedList<Token> analyzedTokens, String fileName) {
        tokens = analyzedTokens;
        compiledTokens = new LinkedList<>();
        symbolTable = new SymbolTable();
        className = tokens.get(1).value;
        writer = new VMWriter(fileName, className);
        compileClass();
        writer.close();
    }

    private void compileClass() {
        // get first token (should be CLASS)
        compiledTokens.add(tokens.poll());

        // retrieve class identifier
        compiledTokens.add(tokens.poll());

        // retrieve { symbol
        compiledTokens.add(tokens.poll());

        // compile variable/field declarations
        compileClassVarDec();

        // compile subroutine declarations
        compileSubroutineDec();

        // retrieve } symbol
        compiledTokens.add(tokens.poll());
    }

    private void compileClassVarDec() {
        // Look at next token and if it begins a variable declaration, compile it
        String next = getNextTokenValue();
        while (next.equals("static") || next.equals("field")) {

            varDecWithSymbols();

            // retrieve ; symbol ending current set of declarations
            compiledTokens.add( tokens.poll());

            next = getNextTokenValue();
        }
    }

    private void compileSubroutineDec() {
        // Look at next token and if it begins a subroutine declaration, compile it
        String next = getNextTokenValue();
        while (next.equals("function") || next.equals("method") || next.equals("constructor")) {

            symbolTable.startSubroutine();
            if (next.equals("method")) {
                symbolTable.define("this", className, "arg");
            }

            // add beginning keyword
            compiledTokens.add(tokens.poll());
            // add return type
            compiledTokens.add(tokens.poll());
            // add name
            String functionName = getNextTokenValue();
            compiledTokens.add(tokens.poll());
            // add parameter list
            compileParameterList();
            // add body
            compileSubroutineBody(functionName, next);

            next = getNextTokenValue();
        }
    }

    private void compileParameterList() {
        // retrieve (
        compiledTokens.add(tokens.poll());

        // add each token in parameter list, halting at )
        String next = getNextTokenValue();
        while (!next.equals(")")) {
            if (next.equals(",")) { compiledTokens.add(tokens.poll()); }
            String type = getNextTokenValue();
            compiledTokens.add(tokens.poll());

            symbolTable.define(getNextTokenValue(), type, "arg");
            compiledTokens.add( tokens.poll());
            next = getNextTokenValue();
        }

        // retrieve )
        compiledTokens.add(tokens.poll());
    }

    private void compileSubroutineBody(String name, String kind) {
        // retrieve { symbol
        compiledTokens.add(tokens.poll());

        // add all variable declarations
        compileVarDec();
        writer.writeFunction(name, symbolTable.varCount("local"));

        if (kind.equals("method")) {
            writer.writePush("argument", 0);
            writer.writePop("pointer", 0);
        } else if (kind.equals("constructor")) {
            writer.writePush("constant", symbolTable.varCount("field"));
            writer.writeCall("Memory.alloc", 1);
            writer.writePop("pointer", 0);
        }

        // add all statements
        compileStatements();

        // retrieve } symbol
        compiledTokens.add(tokens.poll());
    }

    private void compileVarDec() {
        // get each variable declaration and compile
        String next = getNextTokenValue();
        while (next.equals("var")) {

            varDecWithSymbols();

            // retrieve ; symbol ending current set of declarations
            compiledTokens.add(tokens.poll());

            next = getNextTokenValue();
        }
    }

    private void compileStatements() {
        // Look at each statement and compile accordingly
        String next = getNextTokenValue();
        while (next.equals("let")
                || next.equals("if")
                || next.equals("while")
                || next.equals("do")
                || next.equals("return")) {

            switch(next) {
                case "let":     compileLet();
                                break;
                case "if":      compileIf();
                                break;
                case "while":   compileWhile();
                                break;
                case "do":      compileDo();
                                break;
                case "return":  compileReturn();
                                break;
            }

            next = getNextTokenValue();
        }
    }

    private void compileLet() {
        // add statement keyword
        compiledTokens.add(tokens.poll());

        // add varName
        String name = getNextTokenValue();
        String kind = symbolTable.kindOf(name);
        int index = symbolTable.indexOf(name);
        compiledTokens.add(tokens.poll());

        // add expressions, possibly [ ]
        String next = getNextTokenValue();
        if (next.equals("[")) {
            compiledTokens.add(tokens.poll()); // [
            compileExpression();
            writer.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
            compiledTokens.add(tokens.poll()); // ]
            writer.writeArithmetic("+");

            compiledTokens.add(tokens.poll()); // =
            compileExpression();
            writer.writePop("temp", 0);
            writer.writePop("pointer", 1);
            writer.writePush("temp", 0);
            writer.writePop("that", 0);
        } else {
            // add = symbol
            compiledTokens.add(tokens.poll());

            // add expression
            compileExpression();
            writer.writePop(kind, index);
        }


        // add ; symbol
        compiledTokens.add(tokens.poll());
    }

    private void compileIf() {
        int id = ifCount;
        ifCount++;

        // add statement keyword
        compiledTokens.add(tokens.poll());

        // add ( symbol
        compiledTokens.add(tokens.poll());

        // add expression
        compileExpression();
        writer.writeArithmetic("!");
        writer.writeIf("IF_TRUE" + id);

        // add ) symbol
        compiledTokens.add(tokens.poll());

        // add { symbol
        compiledTokens.add(tokens.poll());

        // add statements
        compileStatements();

        // add } symbol
        compiledTokens.add(tokens.poll());

        String next = getNextTokenValue();
        if (next.equals("else")) {
            writer.writeGoto("IF_FALSE" + id);
            // add else
            compiledTokens.add(tokens.poll());

            // add { symbol
            compiledTokens.add(tokens.poll());

            // add statements
            writer.writeLabel("IF_TRUE" + id);
            compileStatements();
            writer.writeLabel("IF_FALSE" + id);

            // add } symbol
            compiledTokens.add(tokens.poll());
        } else {
            writer.writeLabel("IF_TRUE" + id);
        }
    }

    private void compileWhile() {
        int id = whileCount;
        whileCount++;

        writer.writeLabel("WHILE_EXP" + id);

        // add statement keyword
        compiledTokens.add(tokens.poll());

        // add ( symbol
        compiledTokens.add(tokens.poll());

        // add expression
        compileExpression();
        writer.writeArithmetic("!");
        writer.writeIf("WHILE_END" + id);

        // add ) symbol
        compiledTokens.add(tokens.poll());

        // add { symbol
        compiledTokens.add(tokens.poll());

        // add statements
        compileStatements();
        writer.writeGoto("WHILE_EXP" + id);
        writer.writeLabel("WHILE_END" +id);

        // add } symbol
        compiledTokens.add(tokens.poll());
    }

    private void compileDo() {
        // add statement keyword
        compiledTokens.add(tokens.poll());

        // add subroutine call name
        String name = getNextTokenValue();
        int numArgs = symbolTable.contains(name) ? 1 : 0;
        compiledTokens.add(tokens.poll()); // name | ( className | varName )

        // determine call type,
        String next = getNextTokenValue();
        if (next.equals("(")) {
            compiledTokens.add(tokens.poll()); // (
            writer.writePush("pointer", 0);
            numArgs += compileExpressionList() + 1;
            compiledTokens.add(tokens.poll()); // )
            name = className + "." + name;
        } else {
            compiledTokens.add(tokens.poll()); // .
            if (symbolTable.contains(name)) {
                writer.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
                name = symbolTable.typeOf(name);
            }
            name = name + "." + getNextTokenValue();
            compiledTokens.add(tokens.poll()); // name
            compiledTokens.add(tokens.poll()); // (
            numArgs += compileExpressionList();
            compiledTokens.add(tokens.poll()); // )
        }

        // add ; symbol
        compiledTokens.add(tokens.poll());

        writer.writeCall(name, numArgs);
        writer.writePop("temp", 0);
    }

    private void compileReturn() {
        compiledTokens.add(new Token(TokenType.XML_LABEL, "<returnStatement>"));

        // add statement keyword
        compiledTokens.add(tokens.poll());

        // add expressions
        if (getNextTokenValue().equals(";")) {
            writer.writePush("constant", 0);
        } else {
            compileExpression();
        }

        // add ; symbol
        compiledTokens.add(tokens.poll());

        writer.writeReturn();
        compiledTokens.add(new Token(TokenType.XML_LABEL, "</returnStatement>"));
    }

    private void compileExpression() {
        compiledTokens.add(new Token(TokenType.XML_LABEL, "<expression>"));

        // add all terms
        compileTerm();

        String next = getNextTokenValue(); // get following symbol
        while (!next.equals(",") && !next.equals(")") && !next.equals("]") && !next.equals(";")) {
            compiledTokens.add(tokens.poll()); // add operator
            compileTerm();
            writer.writeArithmetic(next);
            next = getNextTokenValue();
        }

        compiledTokens.add(new Token(TokenType.XML_LABEL, "</expression>"));
    }

    private void compileTerm() {
        // determine term type and handle accordingly
        Token next = tokens.peek();
        // varName or subroutineCall
        if (next != null && next.type.equals(TokenType.IDENTIFIER)) {
            compiledTokens.add(tokens.poll());
            String name = next.value;
            int nArgs = 0;
            next = tokens.peek();
            if (next != null && next.value.equals("[")) { // array
                compiledTokens.add(tokens.poll()); // [
                compileExpression();
                writer.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
                compiledTokens.add(tokens.poll()); // ]
                writer.writeArithmetic("+");
                writer.writePop("pointer", 1);
                writer.writePush("that", 0);
            }  else if (next != null && next.value.equals("(")) { // subroutineCall name(###
                compiledTokens.add(tokens.poll()); // (
                compileExpressionList();
                compiledTokens.add(tokens.poll()); // )
            } else if (next != null && next.value.equals(".")){ // subroutineCall name.###
                compiledTokens.add(tokens.poll()); // .
                if (symbolTable.contains(name)) {
                    writer.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
                    nArgs++;
                }
                String methodName = "." + getNextTokenValue();
                compiledTokens.add(tokens.poll()); // name
                compiledTokens.add(tokens.poll()); // (
                nArgs += compileExpressionList();
                compiledTokens.add(tokens.poll()); // )
                if (symbolTable.contains(name)) {
                    writer.writeCall(symbolTable.typeOf(name) + methodName, nArgs);
                } else {
                    writer.writeCall(name + methodName, nArgs);
                }
            } else if (symbolTable.contains(name)) {
                writer.writePush(symbolTable.kindOf(name), symbolTable.indexOf(name));
            }
        } else if (next != null && next.value.equals("(")) { // ( expression )
            compiledTokens.add(tokens.poll()); // (
            compileExpression();
            compiledTokens.add(tokens.poll()); // )
        } else if (next != null && next.value.equals("~")) { // unaryOp term
            compiledTokens.add(tokens.poll());
            compileTerm();
            writer.writeArithmetic("!");
        } else if (next != null && next.value.equals("-")) {
            compiledTokens.add(tokens.poll());
            compileTerm();
            writer.writeArithmetic("~");
        } else if (next != null && next.type == TokenType.KEYWORD) {
            switch(next.value) {
                case "true":    writer.writePush("constant", 1);
                                writer.writeArithmetic("~");
                                break;
                case "false":
                case "null":
                    writer.writePush("constant", 0);
                                break;
                case "this":    writer.writePush("pointer", 0);
                                break;
            }
            compiledTokens.add(tokens.poll());
        } else {
            if ((tokens.peek() != null ? tokens.peek().type : null) == TokenType.STRING_CONST) {
                Token stringToken = tokens.poll();
                compiledTokens.add(stringToken);
                char[] string = stringToken.value.substring(1, stringToken.value.length() - 1).toCharArray();
                writer.writePush("constant", string.length);
                writer.writeCall("String.new", 1);
                for (char c : string) {
                    writer.writePush("constant", c);
                    writer.writeCall("String.appendChar", 2);
                }
            } else {
                writer.writePush("constant", Integer.parseInt(getNextTokenValue()));
                compiledTokens.add(tokens.poll());
            }
        }
    }

    private int compileExpressionList() {
        // compile all expressions in list
        String next = getNextTokenValue();
        int numArgs = 0;
        while (!next.equals(")")) {
            if (numArgs > 0) { compiledTokens.add(tokens.poll()); }
            compileExpression();
            numArgs++;
            next = getNextTokenValue();
        }

        return numArgs;
    }

    private void varDecWithSymbols() {
        String kind = getNextTokenValue();
        compiledTokens.add(tokens.poll());

        String type = getNextTokenValue();
        compiledTokens.add(tokens.poll());

        while (!(getNextTokenValue().equals(";"))) {
            if ((tokens.peek() != null ? tokens.peek().type : null) == TokenType.IDENTIFIER) {
                symbolTable.define(getNextTokenValue(), type, kind);
            }
            compiledTokens.add(tokens.poll());
        }
    }

    private String getNextTokenValue() {
        assert tokens.peek() != null;
        return tokens.peek().value;
    }
}
