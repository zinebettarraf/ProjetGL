package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
/**
 * Integer literal
 *
 * @author gl23
 * @date 01/01/2022
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol  float_sym=compiler.getSymbolTable().getMap().get("int");
                Type typeExpr= compiler.getEnvironnementTypes().getAssociation().get(float_sym).getType();
                this.setType(typeExpr);
                 return typeExpr;
    }

    

    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    public DVal getDval(){
        return new ImmediateInteger(value);
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        setDval(new ImmediateInteger(value));
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        setDval(new ImmediateInteger(value));
    }
}
