package parser;

import parser.lookupTables.LookupTables;

import static parser.InstructionType.A;
import static parser.InstructionType.C;

public class Instruction {
    private final int ADDRESS_LENGTH = 15;

    private String instruction;
    private InstructionType type;
    private LookupTables lookup;

    public Instruction(String instruction) {
        this.instruction = instruction;
        type = instruction.charAt(0) == '@' ? A : C;
        lookup = LookupTables.getInstance();
    }

    public String translate() {
        return type == A ? translateA() : translateC();
    }

    private String translateA() {
        String address = instruction.substring(1);
        int addressNumber;

        if (Character.isDigit(address.charAt(0))) {
            addressNumber = Integer.parseInt(address);
        } else {
            addressNumber = handleVariable(address);
        }

        String binaryAddress = Integer.toBinaryString(addressNumber);
        binaryAddress = addLeadingZeros(binaryAddress);
        return "0" + binaryAddress;
    }

    private int handleVariable(String name) {
        String variable = lookup.symbol(name);

        if (variable == null) {
            lookup.storeVariable(name);
            variable = lookup.symbol(name);
        }

        return Integer.parseInt(variable);
    }

    private String addLeadingZeros(String binaryLocation) {
        while (binaryLocation.length() < ADDRESS_LENGTH) {
            binaryLocation = "0".concat(binaryLocation);
        }
        return binaryLocation;
    }

    private String translateC() {
        String dest = "";
        String comp = "";
        String jump = "";

        boolean hasAssignment = instruction.indexOf('=') > -1;
        boolean hasJump = instruction.indexOf(';') > -1;
        String[] segments = instruction.split("=|;");

        if (hasAssignment && hasJump) {
            dest = segments[0];
            comp = segments[1];
            jump = segments[2];
        } else if (hasJump) {
            comp = segments[0];
            jump = segments[1];
        } else if (hasAssignment) {
            dest = segments[0];
            comp = segments[1];
        }

        comp = translateComp(comp);
        dest = translateDest(dest);
        jump = translateJump(jump);

        return "111" + comp + dest + jump;
    }

    private String translateComp(String comp) {
        if (comp.isEmpty()) {
            comp = "0000000";
        } else {
            comp = lookup.computation(comp);
        }
        return comp;
    }

    private String translateDest(String dest) {
        if (dest.isEmpty()) {
            dest = "000";
        } else {
            dest = lookup.destination(dest);
        }
        return dest;
    }

    private String translateJump(String jump) {
        if (jump.isEmpty()) {
            jump = "000";
        } else {
            jump = lookup.jump(jump);
        }
        return jump;
    }
}
