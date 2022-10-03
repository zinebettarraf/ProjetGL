package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;
import fr.ensimag.deca.context.Signature;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl23
 * @date 01/01/2022
 */
public class ListExpr extends TreeList<AbstractExpr> {

    
    public Signature verifyExpr(DecacCompiler compiler,
    EnvironmentExp localEnv, ClassDefinition currentClass ,Signature expectedSignature)
    throws ContextualError{
             if(expectedSignature.size() != size()){
                throw new ContextualError("Too much parameters while calling the method",getLocation());
             }
             Signature sig =new Signature();
             Iterator<AbstractExpr> it = this.iterator();
             int i=0;
             while (it.hasNext()){
                 AbstractExpr expr = it.next();
                 try{           
                     expr = expr.verifyRValue(compiler, localEnv, currentClass,expectedSignature.paramNumber(i));
                 }
                 catch(ContextualError e){
                     throw new ContextualError("The method is called with a wrong parameters ", getLocation());
                 }
                 set(i, expr);
                 sig.add(expr.getType());
                 i++;
             }
             return sig;
               

    }
    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> it = this.iterator();
        if(it.hasNext()){
            AbstractExpr first = it.next();
            first.decompile(s);
        }
        while (it.hasNext()){
            AbstractExpr i = it.next();
            s.print(", ");
            i.decompile(s);
        }
    }
}
