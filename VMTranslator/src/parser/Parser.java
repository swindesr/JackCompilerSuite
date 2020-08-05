package parser;

import commands.Command;
import commands.CommandFactory;
import commands.CommandType;
import exceptions.InvalidCommandType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Parser {
    List<String> textToParse;
    List<Command> commands;
    String fileName;

    public Parser(String fileName) {
        try {
            BufferedReader loadedFile = readFile(fileName);
            textToParse = new LinkedList<>(splitFileByLine(loadedFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        commands = new LinkedList<>();
    }

    public List<Command> getCommands() { return commands; }

    public void parse() {
        removeWhitespace(textToParse);
        generateCommands(textToParse);
    }

    private BufferedReader readFile(String fileName) throws IOException {
        return new BufferedReader(new FileReader(fileName));
    }

    private LinkedList<String> splitFileByLine(BufferedReader br) throws IOException {
        LinkedList<String> convertedFile = new LinkedList<>();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            convertedFile.add(currentLine);
        }
        return convertedFile;
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

    public void generateCommands(List<String> text) {
        CommandFactory cf = new CommandFactory();
        AtomicInteger commandId = new AtomicInteger(0);

        text.forEach(commandText -> {
            try {
                CommandType type = getCommandType(commandText);
                String arg1 = getFirstArgument(commandText, type);
                int arg2 = getSecondArgument(commandText, type);

                Command command = cf.makeCommand(type, arg1, arg2, commandId.get(), fileName);

                commands.add(command);
                commandId.getAndIncrement();
            } catch (InvalidCommandType e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private CommandType getCommandType(String command) throws InvalidCommandType {
        String firstWord = command;
        if(firstWord.contains(" "))
            firstWord = firstWord.substring(0, firstWord.indexOf(" "));

        switch (firstWord) {
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not": return CommandType.C_ARITHMETIC;
            case "push": return CommandType.C_PUSH;
            case "pop": return CommandType.C_POP;
            case "label": return CommandType.C_LABEL;
            case "goto": return CommandType.C_GOTO;
            case "if-goto": return CommandType.C_IF;
            case "call": return CommandType.C_CALL;
            case "function": return CommandType.C_FUNCTION;
            case "return": return CommandType.C_RETURN;
            default: throw new InvalidCommandType(command + " is not a valid command.");
        }
    }

    private String getFirstArgument(String commandText, CommandType type) {
        String arg = commandText;
        switch(type) {
            case C_POP:
            case C_PUSH:
            case C_CALL:
            case C_FUNCTION:
                arg = arg.substring(arg.indexOf(" ") + 1, arg.lastIndexOf(" "));
            case C_LABEL:
            case C_GOTO:
            case C_IF:
                arg = arg.substring(arg.indexOf(" ") + 1);
        }
        return arg;
    }

    private int getSecondArgument(String commandText, CommandType type) {
        int arg = 0;
        switch(type) {
            case C_POP:
            case C_PUSH:
            case C_CALL:
            case C_FUNCTION:
                arg = Integer.parseInt(commandText.substring(commandText.lastIndexOf(' ') + 1));
        }

        return arg;
    }
}
