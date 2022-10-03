package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import javax.sound.sampled.BooleanControl;

import org.antlr.v4.runtime.atn.BlockEndState;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.B;
import fr.ensimag.ima.pseudocode.instructions.BRA;


/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

                Symbol  float_sym=compiler.getSymbolTable().getMap().get("boolean");
                Type typeExpr=compiler.getEnvironnementTypes().getAssociation().get(float_sym).getType();
                this.setType(typeExpr);
                return typeExpr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (!value) {
            setDval(new ImmediateInteger(0));  
        }
        else {
            setDval(new ImmediateInteger(1));
        }
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (!value) {
            setDval(new ImmediateInteger(0));  
        }
        else {
            setDval(new ImmediateInteger(1));
        }
    }

    @Override
    protected void codeGen(DecacCompiler compiler, boolean branch, Label label) {
        if(value){
            if(branch){
                compiler.addInstruction(new BRA(label));
            }
        }
        else{
            Not not = new Not(new BooleanLiteral(!value));
            not.codeGen(compiler, branch, label);
        }
    }

    @Override
    protected void codeGenARM(DecacCompiler compiler, boolean branch, Label label) {
        if(value){
            if(branch){
                compiler.addInstruction(new B(label));
            }
        }
        else{
            Not not = new Not(new BooleanLiteral(!value));
            not.codeGenARM(compiler, branch, label);
        }
    }

    @Override
    public DVal getDval(){
        if (!value) {
            return new ImmediateInteger(0);  
        }
        else {
            return new ImmediateInteger(1);
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }
}
