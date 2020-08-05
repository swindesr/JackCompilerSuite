package parser.lookupTables;

import java.util.HashMap;
import java.util.Map;

public class LookupTables {
    private static LookupTables INSTANCE;
    private Map<String, String> computations;
    private Map<String, String> destinations;
    private Map<String, String> jumps;
    private Map<String, String> symbols;
    private int variableCount;

    private LookupTables() {
        computations = new HashMap<>();
        destinations = new HashMap<>();
        jumps = new HashMap<>();
        symbols = new HashMap<>();
        populateComputations();
        populateDestinations();
        populateJumps();
        populateSymbols();

        // account for the reserved symbols in populateSymbols()
        variableCount = 16;
    }

    public static LookupTables getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LookupTables();
        }
        return INSTANCE;
    }

    public String destination(String dest) { return destinations.get(dest); }

    public String computation(String comp) { return computations.get(comp); }

    public String jump(String jump) { return jumps.get(jump); }

    public String symbol(String symbol) { return symbols.get(symbol); }

    public void storeLabel(String name, String value) { symbols.put(name, value); }

    public void storeVariable(String name) {
        if (!symbols.containsKey(name)) {
            symbols.put(name, String.valueOf(variableCount));
            variableCount++;
        }
    }

    private void populateComputations() {
        computations.put("0",   "0101010");
        computations.put("1",   "0111111");
        computations.put("-1",  "0111010");
        computations.put("D",   "0001100");
        computations.put("A",   "0110000");
        computations.put("!D",  "0001101");
        computations.put("!A",  "0110001");
        computations.put("-D",  "0001111");
        computations.put("-A",  "0110011");
        computations.put("D+1", "0011111");
        computations.put("A+1", "0110111");
        computations.put("D-1", "0001110");
        computations.put("A-1", "0110010");
        computations.put("D+A", "0000010");
        computations.put("D-A", "0010011");
        computations.put("A-D", "0000111");
        computations.put("D&A", "0000000");
        computations.put("D|A", "0010101");
        computations.put("M",   "1110000");
        computations.put("!M",  "1110001");
        computations.put("-M",  "1110011");
        computations.put("M+1", "1110111");
        computations.put("M-1", "1110010");
        computations.put("D+M", "1000010");
        computations.put("D-M", "1010011");
        computations.put("M-D", "1000111");
        computations.put("D&M", "1000000");
        computations.put("D|M", "1010101");
    }

    private void populateDestinations() {
        destinations.put("null", "000");
        destinations.put("M",    "001");
        destinations.put("D",    "010");
        destinations.put("MD",   "011");
        destinations.put("A",    "100");
        destinations.put("AM",   "101");
        destinations.put("AD",   "110");
        destinations.put("AMD",  "111");
    }

    private void populateJumps() {
        jumps.put("null", "000");
        jumps.put("JGT",  "001");
        jumps.put("JEQ",  "010");
        jumps.put("JGE",  "011");
        jumps.put("JLT",  "100");
        jumps.put("JNE",  "101");
        jumps.put("JLE",  "110");
        jumps.put("JMP",  "111");
    }

    private void populateSymbols() {
        symbols.put("R0",  "0");
        symbols.put("R1",  "1");
        symbols.put("R2",  "2");
        symbols.put("R3",  "3");
        symbols.put("R4",  "4");
        symbols.put("R5",  "5");
        symbols.put("R6",  "6");
        symbols.put("R7",  "7");
        symbols.put("R8",  "8");
        symbols.put("R9",  "9");
        symbols.put("R10", "10");
        symbols.put("R11", "11");
        symbols.put("R12", "12");
        symbols.put("R13", "13");
        symbols.put("R14", "14");
        symbols.put("R15", "15");
        symbols.put("SP",  "0");
        symbols.put("LCL", "1");
        symbols.put("ARG", "2");
        symbols.put("THIS","3");
        symbols.put("THAT","4");
        symbols.put("KBD", "24576");
        symbols.put("SCREEN", "16384");
    }
}