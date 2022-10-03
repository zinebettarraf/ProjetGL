package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.MULARM;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.instructions.SDIVARM;
import fr.ensimag.ima.pseudocode.instructions.SUBARM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type TypeLeftOperand =this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type TypeRightOperand =this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                this.getLeftOperand().setType(TypeLeftOperand);
                this.getRightOperand().setType(TypeRightOperand);
                if (!TypeLeftOperand.sameType(TypeRightOperand)){
                    throw new ContextualError("The two operands are not of the same type in the assigned expression",this.getLeftOperand().getLocation());
                }
                else{
                    this.setType(TypeLeftOperand);
                    return TypeLeftOperand;
                }
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGen(DecacCompiler compiler) {
        compiler.addInstruction(new REM(this.getRightOperand().getDval() , this.getLeftOperand().getReg()));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getOpOverflow()));
        }
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
        GPRegister reg;
        GPRegister div = compiler.getRegManager().getFreeRegister();
        GPRegister mul = compiler.getRegManager().getFreeRegister();
        if (getRightOperand().getReg() != getRightOperand().getARMDval()) {
            reg = compiler.getRegManager().getFreeRegister();
            compiler.addInstruction(new MOV(reg, getRightOperand().getARMDval()));
        }
        else {
            reg = (GPRegister) getRightOperand().getARMDval();
        }
        compiler.addInstruction(new SDIVARM(div, this.getLeftOperand().getReg(), reg));
        compiler.addInstruction(new MULARM(mul, div, reg));
        compiler.addInstruction(new SUBARM(this.getLeftOperand().getReg(), reg));
        compiler.getRegManager().freeRegister(reg);
        compiler.getRegManager().freeRegister(div);
        compiler.getRegManager().freeRegister(mul);
    
        /*if (this.getLeftOperand().getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addInstruction(new BOV(compiler.getOpOverflow()));
            }
        }*/ 
    }
}
