package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.ADDARM;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.MVN;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.ima.pseudocode.instructions.SUBARM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOperand=this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getOperand().setType(typeOperand);
        if (!(typeOperand.isFloat() || typeOperand.isInt())){
            throw new ContextualError("Unary minus must be followed by a float or an int ",this.getLocation());
        }
        this.setType(typeOperand);
        return typeOperand;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        getOperand().setReg(getReg());;
        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new OPP(getOperand().getDval(), getReg()));
        setDval(getReg());

    }
    
    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        GPRegister reg;
        if (getReg() == null) {
            reg = compiler.getRegManager().getFreeRegister();
        }
        else {
            reg = getReg();
        }
        getOperand().setReg(reg);
        getOperand().codeGenARMInst(compiler);
        if (getOperand() instanceof Identifier || this.getOperand() instanceof AbstractReadExpr) {
            compiler.addInstruction(new LDR(getOperand().getReg(), getOperand().getDval()));
            compiler.addInstruction(new LDR(getOperand().getReg(), new ARMRegisterOffset(getOperand().getReg())));
            
            if (getOperand() instanceof AbstractReadExpr) {
                compiler.getReadVars().add(getOperand().getDval());
                compiler.getReadVars().add(getOperand().getReg());
            }
            getOperand().setDval(getOperand().getReg());
        }
        compiler.addInstruction(new MVN(getOperand().getReg(), getOperand().getARMDval()));        
        compiler.addInstruction(new ADDARM(getOperand().getReg(), new ImmediateInteger(1)));
    
        setReg(getOperand().getReg());
        setDval(getReg());
    }

}
