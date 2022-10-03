package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.TertiaryARMInstructions;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

public class MULARM extends TertiaryARMInstructions {

    public MULARM(GPRegister op1, GPRegister op2, DVal op3) {
        super(op1, op2, op3);
    }

    public MULARM(GPRegister op1, DVal op2) {
        this(op1, op1, op2);
    }
}
