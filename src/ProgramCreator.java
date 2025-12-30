import java.util.*;

class ProgramCreator {
    private List<Instruction> code = new ArrayList<>();
    private Map<String, Integer> labels = new HashMap<>();
    private Map<Integer, String> jumps = new HashMap<>();

    public void label(String name) { labels.put(name, code.size()); }
    public void add(OpCode op, int val, Mode m) { code.add(new Instruction(op, val, m)); }
    public void jump(OpCode op, String label) {
        jumps.put(code.size(), label);
        add(op, 0, Mode.VAL);
    }
    public List<Instruction> getProgram() {
        for (var entry : jumps.entrySet()) {
            code.get(entry.getKey()).operand = labels.get(entry.getValue());
        }
        return code;
    }
}