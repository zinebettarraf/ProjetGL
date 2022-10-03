package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            
                Type typeLeftOperand =this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type typeRightOperand =this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                if(typeLeftOperand.isClass()  || typeRightOperand.isClass() ){
                    throw new ContextualError("Boolean operations are not allowed for type class ", getLocation());
                }
                this.getLeftOperand().setType(typeLeftOperand);
                this.getRightOperand().setType(typeRightOperand);
                if (!typeLeftOperand.isBoolean() || !typeRightOperand.isBoolean()){
                    throw new ContextualError("The two operands must be boolean expression ",this.getLeftOperand().getLocation());
                }
                else{
                    Symbol  boolean_sym=compiler.getSymbolTable().getMap().get("boolean");
                    this.setType(compiler.getEnvironnementTypes().getAssociation().get(boolean_sym).getType());
                    return compiler.getEnvironnementTypes().getAssociation().get(boolean_sym).getType();       
                }
    }

    @Override 
    protected void codeGenInst(DecacCompiler compiler){
        codeGenBool(compiler, false, "result");
        setDval(getReg());
    }

    @Override 
    protected void codeGenARMInst(DecacCompiler compiler){
        codeGenARMBool(compiler, false, "result");
        setDval(getReg());
    }
}
