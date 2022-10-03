package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;
import fr.ensimag.deca.context.Signature;
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print("(");
        Iterator<AbstractDeclParam> it = this.iterator();
        if (it.hasNext()){
            AbstractDeclParam i = it.next();
            i.decompile(s);
        }
        while (it.hasNext()){
            s.print(", ");
            AbstractDeclParam i = it.next();
            i.decompile(s); 
        }
        s.print(")");
    }


    Signature verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv,
                     ClassDefinition currentClass) throws ContextualError {
                      
                Signature sig=new Signature();
                for(AbstractDeclParam param: this.getList()){
                    sig.add(param.verifyDeclParam(compiler, localEnv, currentClass));
                }
                return sig;
    }

    //@Override
    protected void codeGenListDeclParam(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
        /**  A FAIRE: traiter les déclarations de champs
        for(AbstractDeclParam param: getList()){
            param.codeGenDeclParam(compiler);
        } */
    }
}
