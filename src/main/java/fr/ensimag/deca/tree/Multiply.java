package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.MULARM;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected void codeGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        compiler.addInstruction(new MUL(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));

        if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }

        
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        GPRegister reg;
        if (getRightOperand().getReg() != getRightOperand().getARMDval()) {
            reg = compiler.getRegManager().getFreeRegister();
            compiler.addInstruction(new MOV(reg, getRightOperand().getARMDval()));
        }
        else {
            reg = (GPRegister) getRightOperand().getARMDval();
        }
        compiler.addInstruction(new MULARM(this.getLeftOperand().getReg(), reg));
        compiler.getRegManager().freeRegister(reg);
    
        /*if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }*/ 
    }
}
