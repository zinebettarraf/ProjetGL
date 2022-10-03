package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

   

    @Override
    protected void codeGen(DecacCompiler compiler, boolean branch, Label label) {
        Not not = new Not(new And(new Not(getLeftOperand()), new Not(getRightOperand())));
        not.setReg(getReg());
        not.codeGen(compiler, branch, label);
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler, boolean branch, Label label) {
        GPRegister reg;
        if (getReg() == null) {
            reg = compiler.getRegManager().getFreeRegister();
        }
        else {
            reg = getReg();
        }
        getLeftOperand().setReg(reg);
        getRightOperand().setReg(reg);
        Not not = new Not(new And(new Not(getLeftOperand()), new Not(getRightOperand())));
        setReg(reg);
        not.codeGenARM(compiler, branch, label);
    }
}
