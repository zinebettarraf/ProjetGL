package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class TertiaryARMInstructions extends Instruction {

    private Operand operand1, operand2, operand3;

    public Operand getOperand1() {
        return operand1;
    }

    public Operand getOperand2() {
        return operand2;
    }

    public Operand getOperand3() {
        return operand3;
    }

    protected TertiaryARMInstructions(Operand op1, Operand op2, Operand op3) {
        Validate.notNull(op1);
        Validate.notNull(op2);
        Validate.notNull(op3);
        this.operand1 = op1;
        this.operand2 = op2;
        this.operand3 = op3;
    }

    @Override
    String getName() {
        String name = this.getClass().getSimpleName();
        return name.substring(0, name.length()-3);
    }

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(getOperand1());
        s.print(", ");
        s.print(getOperand2());
        s.print(", ");
        s.print(getOperand3());
    }

}
