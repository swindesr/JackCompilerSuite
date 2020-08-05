package commands;

public class CommandFactory {
    public Command makeCommand(CommandType type, String arg1, int arg2, int id, String fileName) {
        switch(type) {
            case C_ARITHMETIC: return new Arithmetic(arg1, id);
            case C_PUSH: return new Push(arg1, arg2, id, fileName);
            case C_POP: return new Pop(arg1, arg2, id, fileName);
            case C_LABEL: return new Label(arg1, id, fileName);
            case C_GOTO: return new Goto(arg1, id, fileName);
            case C_IF: return new If(arg1, id, fileName);
            case C_CALL: return new Call(arg1, arg2, id, fileName);
            case C_FUNCTION: return new C_Function(arg1, arg2, id, fileName);
            case C_RETURN: return new Return(arg1, id);
            default:
                throw new UnsupportedOperationException("Attempted to create a command without a proper type.");
        }
    }
}
