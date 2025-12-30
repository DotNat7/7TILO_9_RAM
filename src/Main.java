import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RamExecutor ram = new RamExecutor();

        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  ÚKOL 1b: Program pro výpočet součinu   │");
        System.out.println("└─────────────────────────────────────────┘");
        ProgramCreator p1 = new ProgramCreator();
        p1.add(OpCode.LOAD, 1, Mode.VAL);
        p1.add(OpCode.STORE, 10, Mode.REG);
        p1.label("LOOP");
        p1.add(OpCode.READ, 11, Mode.REG);
        p1.add(OpCode.LOAD, 11, Mode.REG);
        p1.jump(OpCode.JZERO, "EXIT");
        p1.add(OpCode.LOAD, 10, Mode.REG);
        p1.add(OpCode.MUL, 11, Mode.REG);
        p1.add(OpCode.STORE, 10, Mode.REG);
        p1.jump(OpCode.JUMP, "LOOP");
        p1.label("EXIT");
        p1.add(OpCode.WRITE, 10, Mode.REG);
        p1.add(OpCode.HALT, 0, Mode.VAL);
        List<Instruction> prog1 = p1.getProgram();

        runTestSoucin(ram, prog1, new int[]{2, 3, 4}, 24);
        runTestSoucin(ram, prog1, new int[]{5, 6}, 30);
        runTestSoucin(ram, prog1, new int[]{1, 2, 3, 4, 5}, 120);

        System.out.println("\n┌──────────────────────────────────────────────┐");
        System.out.println("│  ÚKOL 2: Rozpoznávání jazyka {1^n 2^n 3^n}   │");
        System.out.println("└──────────────────────────────────────────────┘");

        ProgramCreator p2 = new ProgramCreator();
        p2.label("START");
        p2.add(OpCode.READ, 5, Mode.REG);
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.jump(OpCode.JZERO, "CHECK");
        p2.add(OpCode.SUB, 1, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_1");
        p2.jump(OpCode.JUMP, "REJECT");

        p2.label("INC_1");
        p2.add(OpCode.LOAD, 2, Mode.REG);
        p2.add(OpCode.ADD, 1, Mode.VAL);
        p2.add(OpCode.STORE, 2, Mode.REG);
        p2.add(OpCode.READ, 5, Mode.REG);
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.jump(OpCode.JZERO, "CHECK");
        p2.add(OpCode.SUB, 1, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_1");
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.add(OpCode.SUB, 2, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_2");
        p2.jump(OpCode.JUMP, "REJECT");

        p2.label("INC_2");
        p2.add(OpCode.LOAD, 3, Mode.REG);
        p2.add(OpCode.ADD, 1, Mode.VAL);
        p2.add(OpCode.STORE, 3, Mode.REG);
        p2.add(OpCode.READ, 5, Mode.REG);
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.jump(OpCode.JZERO, "CHECK");
        p2.add(OpCode.SUB, 2, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_2");
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.add(OpCode.SUB, 3, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_3");
        p2.jump(OpCode.JUMP, "REJECT");

        p2.label("INC_3");
        p2.add(OpCode.LOAD, 4, Mode.REG);
        p2.add(OpCode.ADD, 1, Mode.VAL);
        p2.add(OpCode.STORE, 4, Mode.REG);
        p2.add(OpCode.READ, 5, Mode.REG);
        p2.add(OpCode.LOAD, 5, Mode.REG);
        p2.jump(OpCode.JZERO, "CHECK");
        p2.add(OpCode.SUB, 3, Mode.VAL);
        p2.jump(OpCode.JZERO, "INC_3");
        p2.jump(OpCode.JUMP, "REJECT");

        p2.label("CHECK");
        p2.add(OpCode.LOAD, 2, Mode.REG);
        p2.add(OpCode.SUB, 3, Mode.REG);
        p2.jump(OpCode.JZERO, "CHECK_2");
        p2.jump(OpCode.JUMP, "REJECT");
        p2.label("CHECK_2");
        p2.add(OpCode.LOAD, 3, Mode.REG);
        p2.add(OpCode.SUB, 4, Mode.REG);
        p2.jump(OpCode.JZERO, "ACCEPT");

        p2.label("REJECT");
        p2.add(OpCode.LOAD, 0, Mode.VAL);
        p2.jump(OpCode.JUMP, "FINISH");
        p2.label("ACCEPT");
        p2.add(OpCode.LOAD, 1, Mode.VAL);
        p2.label("FINISH");
        p2.add(OpCode.WRITE, 0, Mode.REG);
        p2.add(OpCode.HALT, 0, Mode.VAL);

        List<Instruction> prog2 = p2.getProgram();
        System.out.println("Program má " + prog2.size() + " instrukcí\n");

        runTestJazyk(ram, prog2, "ε (prázdný řetězec)", new int[]{}, 1);
        runTestJazyk(ram, prog2, "1 2 3", new int[]{1, 2, 3}, 1);
        runTestJazyk(ram, prog2, "1^2 2^2 3^2", new int[]{1, 1, 2, 2, 3, 3}, 1);
        runTestJazyk(ram, prog2, "1^3 2^3 3^3", new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3}, 1);
        runTestJazyk(ram, prog2, "1 2^2 3", new int[]{1, 2, 2, 3}, 0);
    }

    private static void runTestSoucin(RamExecutor ram, List<Instruction> p, int[] in, int target) {
        ram.load(p, in); ram.run();
        List<Integer> out = ram.getOutput();
        System.out.println("\nVstup: " + Arrays.toString(in).replace("]", ", 0]"));
        if (ram.isLimitExceeded()) System.out.println("Varování: Překročen maximální počet kroků!");
        System.out.println("Výstup: " + out);
        boolean ok = !out.isEmpty() && out.get(0) == target;
        System.out.println("Očekáváno: " + target + " | " + (ok ? "✓ SPRÁVNĚ" : "✗ CHYBA"));
    }

    private static void runTestJazyk(RamExecutor ram, List<Instruction> p, String lbl, int[] in, int target) {
        ram.load(p, in); ram.run();
        int res = ram.getOutput().isEmpty() ? 0 : ram.getOutput().get(0);
        System.out.println("Test: " + lbl + "\nVstup: " + Arrays.toString(in).replaceAll("[\\[\\],]", ""));
        System.out.println("Výstup: " + res + " (" + (res == 1 ? "PŘIJATO" : "ODMÍTNUTO") + ")");
        boolean ok = (res == target);
        System.out.println((ok ? "✓ SPRÁVNĚ" : "✗ CHYBA") + " (očekáváno: " + (target == 1 ? "PŘIJMOUT" : "ODMÍTNOUT") + ")\n");
    }
}