package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;

import java.io.PrintStream;

/**
 *
 * @author gl23
 * @date 10/01/2022
 */
public class Null extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

                Type typeExpr=new NullType(compiler.getSymbolTable().create("null"));
                this.setType(typeExpr);
                return typeExpr;
    }

    @Override
    public DVal getDval(){
        return new NullOperand();
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        setDval(new NullOperand());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
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