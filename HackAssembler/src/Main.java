import io.FileIn;
import io.FileOut;
import parser.Parser;
import parser.lookupTables.LookupTables;

public class Main {
    public static void main(String[] args) {
        String fileName = args[0];
        FileIn input = new FileIn(fileName + ".asm");

        LookupTables.getInstance();
        Parser parser = new Parser(input);

        new FileOut(parser.parse(), fileName + ".hack");
    }
}
