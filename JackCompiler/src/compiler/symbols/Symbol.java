package compiler.symbols;

public class Symbol {
    private String type;
    private String kind;
    private int runningIndex;

    public Symbol(String type, String kind, int index) {
        this.type = type;
        this.kind = kind;
        runningIndex = index;
    }

    public String getKind() { return kind; }
    public String getType() { return type; }
    public int getIndex() { return runningIndex; }
}
