package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */

public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    /* forcer l'utilisateur à entrer une valeur float=> tester en partie C*/
    /*  if (this.getType().getName()!="float"){
            throw new ContextualError("you must enter an int value ",this.getLocation());
        }

        else{
            return this.getType(); 
        }    */
        Symbol  float_sym=compiler.getSymbolTable().getMap().get("float");
        Type typeExpr=compiler.getEnvironnementTypes().getAssociation().get(float_sym).getType();
        this.setType(typeExpr);
        return typeExpr;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
    protected void codeGenInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        compiler.addInstruction(new RFLOAT());
        super.codeGenInst(compiler);
        
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        
    }

}
