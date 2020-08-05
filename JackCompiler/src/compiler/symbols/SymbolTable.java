package compiler.symbols;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> classTable;
    private Map<String, Symbol> subroutineTable;
    private int staticCount, fieldCount, argCount, varCount = 0;

    public SymbolTable() {
        classTable = new HashMap<>();
        subroutineTable = new HashMap<>();
    }

    public void startSubroutine() {
        subroutineTable.clear();
        argCount = 0;
        varCount = 0;
    }

    public void define(String name, String type, String kind) {
        switch (kind) {
            case "static":
                classTable.put(name, new Symbol(type, kind, staticCount));
                staticCount++;
                break;
            case "field":
                classTable.put(name, new Symbol(type, kind, fieldCount));
                fieldCount++;
                break;
            case "arg":
                subroutineTable.put(name, new Symbol(type, "argument", argCount));
                argCount++;
                break;
            case "var":
                subroutineTable.put(name, new Symbol(type, "local", varCount));
                varCount++;
                break;
        }
    }

    public int varCount(String kind) {
        switch (kind) {
            case "static":      return staticCount;
            case "field":       return fieldCount;
            case "argument":    return argCount;
            case "local":       return varCount;
            default:            return -1;
        }
    }

    public String kindOf(String name) {
        if (!classTable.containsKey(name) && !subroutineTable.containsKey(name)) {
            return "";
        } else {
            return subroutineTable.containsKey(name) ? subroutineTable.get(name).getKind() : classTable.get(name).getKind();
        }
    }

    public String typeOf(String name) {
        return subroutineTable.containsKey(name) ? subroutineTable.get(name).getType() : classTable.get(name).getType();
    }

    public int indexOf(String name) {
        return subroutineTable.containsKey(name) ? subroutineTable.get(name).getIndex() : classTable.get(name).getIndex();
    }

    public boolean contains(String name) {
        return classTable.containsKey(name) || subroutineTable.containsKey(name);
    }
}
