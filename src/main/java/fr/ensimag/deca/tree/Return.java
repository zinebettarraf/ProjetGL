package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * To Return a Value in the defined methods
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Return extends AbstractInst {

    AbstractExpr exp;
    public Return (AbstractExpr exp){
        this.exp = exp;
    }

    @Override
    public void verifyInst(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        Type voidType=(compiler.getEnvironnementTypes().get(compiler.getSymbolTable().create("void"))).getType();   
        if(returnType.sameType(voidType)){
              throw new ContextualError("The return of the method is void ", getLocation());
        }
        exp.verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    @Override 
    public void codeGenARMInst(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s){
        //throw new UnsupportedOperationException("not yet implemented");
        s.print("return ");
        exp.decompile(s);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        //        throw new UnsupportedOperationException("not yet implemented");
        exp.setReg(compiler.getRegManager().getFreeRegister());
        exp.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(exp.getDval(), Register.R0));
        compiler.addInstruction(new BRA(compiler.getLabelManager().getLastFinMethod()));
        compiler.getRegManager().freeRegister(exp.getReg());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.exp.prettyPrint(s, prefix, false);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        this.exp.iter(f);
    }

}