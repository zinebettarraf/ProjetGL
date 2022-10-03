package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.SDIVARM;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected void codeGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        if (this.getLeftOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));
            
        }

        else if (this.getLeftOperand().getType().isFloat()) {
            compiler.addInstruction(new DIV(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));
        }

        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getOpOverflow()));
        }
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler) {
        
        GPRegister reg;
        if (getRightOperand().getReg() != getRightOperand().getARMDval()) {
            reg = compiler.getRegManager().getFreeRegister();
            compiler.addInstruction(new MOV(reg, getRightOperand().getARMDval()));
        }
        else {
            reg = (GPRegister) getRightOperand().getARMDval();
        }
        compiler.addInstruction(new SDIVARM(this.getLeftOperand().getReg(), reg));
        compiler.getRegManager().freeRegister(reg);
    
        /*if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }*/ 
    }

}
