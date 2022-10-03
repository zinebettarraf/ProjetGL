package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDARM;
import fr.ensimag.ima.pseudocode.instructions.BOV;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
 

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected void codeGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        compiler.addInstruction(new ADD(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));
    
        if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }
        
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        compiler.addInstruction(new ADDARM(this.getLeftOperand().getReg(), this.getRightOperand().getARMDval()));
    
        /*if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }*/ 
    }
}
