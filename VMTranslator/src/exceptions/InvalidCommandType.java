package exceptions;

public class InvalidCommandType extends Exception {
    public InvalidCommandType(String message) {
        super(message);
    }
}
