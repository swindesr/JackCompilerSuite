package tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Tokenizer {
    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
            '{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'));
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean",
            "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return"));

    private String currentToken;
    private String currentLine;
    private int tokenCursor;
    private boolean inBlockComment;
    private LinkedList<Token> analyzedTokens;

    public Tokenizer(File file) {
        analyzedTokens = new LinkedList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((currentLine = reader.readLine()) != null) {
                trimAndRemoveComments();
                while (hasMoreTokens() && !inBlockComment) {
                    currentToken = advance();
                    analyzeToken();
                }
                tokenCursor = 0;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public LinkedList<Token> getAnalyzedTokens() { return analyzedTokens; }

    private void trimAndRemoveComments() {
        currentLine = currentLine.trim();

        if (currentLine.startsWith("/*")) inBlockComment = true;

        int commentIndex = currentLine.indexOf("//");
        if (commentIndex != -1) {
            currentLine = currentLine.substring(0, commentIndex);
            currentLine = currentLine.trim();
        }

        if (currentLine.endsWith("*/")) {
            inBlockComment = false;
            currentLine = "";
        }
    }

    private boolean hasMoreTokens() {
        int lineLength = currentLine.length();
        if (lineLength > 0 && tokenCursor < lineLength) {
            while (currentLine.charAt(tokenCursor) == ' ') {
                tokenCursor++;
            }
        }
        return tokenCursor != lineLength && lineLength > 0;
    }

    private String advance() {
        StringBuilder tokenBuilder = new StringBuilder();
        char currentChar;
        boolean isStringConst = false;

        while ((currentChar = currentLine.charAt(tokenCursor)) != ' ' || isStringConst) {
            if (SYMBOLS.contains(currentChar) && !isStringConst) {
                if (tokenBuilder.length() == 0) {
                    tokenBuilder.append(currentChar);
                    tokenCursor++;
                }
                return tokenBuilder.toString();
            } else if (currentChar == '"') {
                isStringConst = !isStringConst;
            }
            tokenBuilder.append(currentLine.charAt(tokenCursor));
            tokenCursor++;
        }
        return tokenBuilder.toString();
    }

    private TokenType tokenType() {
        TokenType type = null;

        if (KEYWORDS.contains(currentToken)) {
            type = TokenType.KEYWORD;
        } else if (SYMBOLS.contains(currentToken.charAt(0))) {
            type = TokenType.SYMBOL;
        } else if (Character.isDigit(currentToken.charAt(0))) {
            if (Integer.parseInt(currentToken) >= 0 && Integer.parseInt(currentToken) <= 32767) {
                type = TokenType.INT_CONST;
            }
        } else if (currentToken.startsWith("\"")
                    && currentToken.endsWith("\"")
                    && !currentToken.contains("\n")
                    && !currentToken.substring(1, currentToken.length() - 1).contains("\"")) {
            type = TokenType.STRING_CONST;
        } else if (!Character.isDigit(currentToken.charAt(0))) {
            type = TokenType.IDENTIFIER;
        }

        return type;
    }

    private void analyzeToken() {
        Token analyzedToken;
        switch (tokenType()) {
            case KEYWORD:       analyzedToken = new Token(tokenType(), keyWord());
                                break;
            case SYMBOL:        analyzedToken = new Token(tokenType(), symbol());
                                break;
            case IDENTIFIER:    analyzedToken = new Token(tokenType(), identifier());
                                break;
            case INT_CONST:     analyzedToken = new Token(tokenType(), intVal());
                                break;
            case STRING_CONST:  analyzedToken = new Token(tokenType(), stringVal());
                                break;
            default:            throw new UnsupportedOperationException("Given invalid token type.");
        }
        analyzedTokens.add(analyzedToken);
    }

    private String keyWord() { return currentToken; }

    private String symbol() {
        switch (currentToken) {
            case "<":   currentToken = "&lt;";
                        break;
            case ">":   currentToken = "&gt;";
                        break;
            case "&":   currentToken = "&amp;";
                        break;
            default:    break;
        }
        return currentToken;
    }

    private String identifier() {
        return currentToken;
    }

    private String intVal() { return currentToken; }

    private String stringVal() { return currentToken; }

}
