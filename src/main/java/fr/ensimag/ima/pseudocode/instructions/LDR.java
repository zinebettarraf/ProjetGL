package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.VarCall;

public class LDR extends BinaryInstructionDValToReg{
    
    public LDR(GPRegister op1, DVal op2) {
        super(op1, op2);
    }

    public LDR(GPRegister r, int i) {
        this(r, new ImmediateInteger(i));
    }

    public LDR(GPRegister r, String s) {
        this(r, new VarCall(s));
    }
}
