package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

public class MVN extends BinaryInstructionDValToReg {
    
    public MVN(GPRegister op1, DVal op2) {
        super(op1, op2);
    }

    public MVN(GPRegister r, int i) {
        this(r, new ImmediateInteger(i));
    }

}
