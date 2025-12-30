import java.util.*;

public class RamExecutor {
    private Map<Integer, Integer> memory = new HashMap<>();
    private Queue<Integer> input = new LinkedList<>();
    private List<Integer> output = new ArrayList<>();
    private List<Instruction> program;
    private int ip = 0;
    private boolean limitExceeded = false;

    public void load(List<Instruction> program, int... inputs) {
        this.program = program;
        this.memory.clear();
        this.output.clear();
        this.input.clear();
        for (int i : inputs) this.input.add(i);
        this.input.add(0);
        this.ip = 0;
        this.limitExceeded = false;
        memory.put(0, 0); memory.put(1, 0);
    }

    private int fetch(int val, Mode mode) {
        if (mode == Mode.VAL) return val;
        if (mode == Mode.REG) return memory.getOrDefault(val, 0);
        return memory.getOrDefault(val + memory.getOrDefault(1, 0), 0);
    }

    public void run() {
        int steps = 0;
        while (ip >= 0 && ip < program.size()) {
            if (steps++ > 5000) { // Limit pro bezpečnost
                limitExceeded = true;
                return;
            }
            Instruction inst = program.get(ip);
            int acc = memory.getOrDefault(0, 0);
            int nextIp = ip + 1;

            switch (inst.op) {
                case READ -> memory.put(inst.operand, input.isEmpty() ? 0 : input.poll());
                case WRITE -> output.add(fetch(inst.operand, inst.mode));
                case LOAD -> memory.put(0, fetch(inst.operand, inst.mode));
                case STORE -> {
                    int addr = (inst.mode == Mode.PTR) ? inst.operand + memory.getOrDefault(1, 0) : inst.operand;
                    memory.put(addr, acc);
                }
                case ADD -> memory.put(0, acc + fetch(inst.operand, inst.mode));
                case SUB -> memory.put(0, acc - fetch(inst.operand, inst.mode));
                case MUL -> memory.put(0, acc * fetch(inst.operand, inst.mode));
                case JUMP -> nextIp = inst.operand;
                case JZERO -> { if (acc == 0) nextIp = inst.operand; }
                case JGTZ -> { if (acc > 0) nextIp = inst.operand; }
                case HALT -> { ip = -1; return; }
                default -> {}
            }
            ip = nextIp;
        }
    }
    public boolean isLimitExceeded() { return limitExceeded; }
    public List<Integer> getOutput() { return output; }
}