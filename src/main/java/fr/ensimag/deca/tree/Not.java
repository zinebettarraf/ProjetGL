package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOperand=this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getOperand().setType(typeOperand);
        if (!typeOperand.isBoolean()){
            throw new ContextualError("Not must be followed by a boolean expresssion",this.getLocation());
        }
        Symbol  float_sym=compiler.getSymbolTable().getMap().get("boolean");
        Type typeExpr=compiler.getEnvironnementTypes().getAssociation().get(float_sym).getType();
        this.setType(typeExpr);
        return typeExpr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().setReg(getReg());
        getOperand().codeGenBool(compiler, true, "result");
        setDval(getReg());
    }

    @Override
    protected void codeGen(DecacCompiler compiler, boolean branch, Label label){
        getOperand().setReg(getReg());
        getOperand().codeGen(compiler, !branch, label);
    }

    @Override
    protected String getOperatorName() {
        return "!";
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
        getOperand().codeGenARMBool(compiler, true, "result");
        /*if (reg != getReg()) {
            compiler.getRegManager().freeRegister(reg);
        }*/
        setReg(getOperand().getReg());
        setDval(getReg());
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler, boolean branch, Label label){
        
        getOperand().codeGenARM(compiler, !branch, label);
        setReg(getOperand().getReg());
    }
}
