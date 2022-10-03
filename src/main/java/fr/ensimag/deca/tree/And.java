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
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    

    @Override
    protected void codeGen(DecacCompiler compiler, boolean branch, Label label) {
        getLeftOperand().setReg(getReg());
        getRightOperand().setReg(getReg());
        if(branch){
            Label fin = compiler.getLabelManager().createLabel("fin");
            getLeftOperand().codeGen(compiler, false, fin);
            getRightOperand().codeGen(compiler, true, label);
            compiler.addLabel(fin);
        }
        else{
            getLeftOperand().codeGen(compiler, false, label);
            getRightOperand().codeGen(compiler, false, label);
        }
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
        if(branch){
            Label fin = compiler.getLabelManager().createLabel("fin");
            getLeftOperand().codeGenARM(compiler, false, fin);
            getRightOperand().codeGenARM(compiler, true, label);
            compiler.addLabel(fin);
        }
        else{
            getLeftOperand().codeGenARM(compiler, false, label);
            getRightOperand().codeGenARM(compiler, false, label);
        }
        setReg(reg);
        setDval(getReg());
    }
}
