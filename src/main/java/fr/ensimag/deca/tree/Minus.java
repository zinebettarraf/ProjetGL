package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.SUBARM;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        compiler.addInstruction(new SUB(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));

        if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        compiler.addInstruction(new SUBARM(this.getLeftOperand().getReg(), this.getRightOperand().getARMDval()));
    
        /*if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }*/ 
    }    
}
