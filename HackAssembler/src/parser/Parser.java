package parser;

import io.FileIn;
import parser.lookupTables.LookupTables;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Parser {
    private List<String> textToParse;
    private LookupTables lookup;

    public Parser(FileIn input) {
        textToParse = input.getText();
        lookup = LookupTables.getInstance();
    }

    public List<String> parse() {
        removeWhitespace(textToParse);
        storeLabels(textToParse);
        translateInstructions(textToParse);
        return textToParse;
    }

    private void removeWhitespace(List<String> text) {
        List<String> parsed = new LinkedList<>();

        text.forEach(line -> {
            line = line.trim();
            if (!entireLineWhitespace(line)) {
                line = removeComments(line);
                parsed.add(line);
            }
        });

        textToParse = parsed;
    }

    private boolean entireLineWhitespace(String line) {
        Boolean isEmpty = line.isBlank();
        Boolean isComment = !isEmpty && line.charAt(0) == '/';
        return isEmpty || isComment;
    }

    private String removeComments(String line) {
        int commentStart = line.indexOf('/');
        if (commentStart > -1)
            line = line.substring(0,commentStart);
        line = line.trim();
        return line;
    }

    private void storeLabels (List<String> text) {
        List<String> parsed = new LinkedList<>();
        AtomicInteger lineCount = new AtomicInteger(0);

        text.forEach(line -> {
            if (line.charAt(0) == '(') {
                storeLabel(line,lineCount);
            } else {
                parsed.add(line);
                lineCount.getAndIncrement();
            }
        });

        textToParse = parsed;
    }

    private void storeLabel(String line, AtomicInteger lineCount) {
        String value = lineCount.toString();
        String labelName = line.substring(1,line.length() - 1);
        lookup.storeLabel(labelName, value);
    }

    private void translateInstructions(List<String> text) {
        List<String> parsed = new LinkedList<>();

        text.forEach(instructionText -> {
            Instruction instruction = new Instruction(instructionText);
            parsed.add(instruction.translate());
        });

        textToParse = parsed;
    }
}
