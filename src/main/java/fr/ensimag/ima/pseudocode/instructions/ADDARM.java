package fr.ensimag.ima.pseudocode.instructions;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.TertiaryARMInstructions;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;

public class ADDARM extends TertiaryARMInstructions {

    public ADDARM(GPRegister op1, GPRegister op2, DVal op3) {
        super(op1, op2, op3);
    }

    public ADDARM(GPRegister op1, DVal op2) {
        this(op1, op1, op2);
    }
}
