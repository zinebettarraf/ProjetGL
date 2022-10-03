package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclMethod c: getList()){
            c.decompile(s);
            s.println("");
        }
    }

        /**
     * Implements non-terminal "list_decl_method" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    int verifyListDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv,
                     ClassDefinition currentClass,int index) throws ContextualError 
                     {
                        int i=index+1;
                for(AbstractDeclMethod method: this.getList()){
                    boolean redefiendMethod=method.verifyDeclMethod(compiler, localEnv, currentClass,i);
                    if (! redefiendMethod){
                         i++;
                    }
                }
                return i;
     
    }

    //@Override
    protected void codeGenListDeclMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
        /**  A FAIRE: traiter les déclarations de champs
        for(AbstractDeclParam param: getList()){
            param.codeGenDeclParam(compiler);
        } */
    }
}
