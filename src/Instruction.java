public class Instruction {
    OpCode op;
    int operand;
    Mode mode;

    public Instruction(OpCode op, int operand, Mode mode) {
        this.op = op;
        this.operand = operand;
        this.mode = mode;
    }
}