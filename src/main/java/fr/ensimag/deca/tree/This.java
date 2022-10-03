package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author gl23
 * @date 10/01/2022
 */
public class This extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(currentClass== null){
            throw new ContextualError("this is only used inside class to refer the object", getLocation());
        }
        else{
             Type thisType=currentClass.getType();
             setType(thisType);
             return thisType;
        }
    }
    
    @Override
    public DVal getDval(){
        return new RegisterOffset(-2, Register.LB);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        //throw new UnsupportedOperationException("not yet implemented");
        /*GPRegister reg = getReg();
        if(getReg() == null){
            reg = compiler.getRegManager().getFreeRegister();
        }
 //       compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), getReg()));
        if(getReg() == null){
            compiler.getRegManager().freeRegister(reg);
        }*/
        setDval(getDval());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}