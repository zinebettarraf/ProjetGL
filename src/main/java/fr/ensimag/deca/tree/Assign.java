package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.VarCall;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.STR;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class  Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
              
                Type typeLeft=this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                if(!getLeftOperand().isSelection){
                    if(  ((Identifier) getLeftOperand()).getDefinition().isMethod() ){
                        throw new ContextualError("we can't assign a value to a method ", getLocation());
                    }
                }
                AbstractExpr exp = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, typeLeft);         
                setRightOperand(exp);
                this.setType(typeLeft);
                return typeLeft ;


    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        GPRegister reg = getReg();
        if(getReg() == null){
            reg = compiler.getRegManager().getFreeRegister();
        }
        
        getRightOperand().setReg(reg);
        getRightOperand().codeGenInst(compiler);
        DVal dval = getRightOperand().getDval();
        

        if(getLeftOperand().getDAddr() == null){
            getLeftOperand().codeGenInst(compiler);
        }
        DAddr destAddr = getLeftOperand().getDAddr();   
        if (dval instanceof Register){
            reg = (GPRegister) dval;
        }
        else{
            compiler.addInstruction(new LOAD(getRightOperand().getDval(), getRightOperand().getReg()));
        }
        compiler.addInstruction(new STORE(reg, destAddr));
        
        if(getReg() == null){
            compiler.getRegManager().freeRegister(reg);
        }
        
        setDval(reg);
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        getRightOperand().codeGenARMInst(compiler);
        if (getRightOperand() instanceof AbstractReadExpr) {
            ((Identifier)getLeftOperand()).getExpDefinition().setOperand((VarCall) getRightOperand().getARMDval());
        }
        if (getRightOperand().getReg() != null) {
            GPRegister reg = compiler.getRegManager().getFreeRegister();
            compiler.addInstruction(new LDR(reg, getLeftOperand().getDval()));
            compiler.addInstruction(new STR(getRightOperand().getReg(), new ARMRegisterOffset(reg)));
            compiler.getRegManager().freeRegister(reg);
            compiler.getRegManager().freeRegister(getRightOperand().getReg());
        }
        compiler.getAssigns().add(getRightOperand());
        //getRightOperand().setReg(null);
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }
}