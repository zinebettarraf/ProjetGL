package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl23
 * @date 01/01/2022
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclVar> it = iterator();
        while (it.hasNext()){
            AbstractDeclVar i = it.next();
            i.decompile(s); 
        }
        
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
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
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                for(AbstractDeclVar var: this.getList()){
                    var.verifyDeclVar(compiler, localEnv, currentClass);
                }
    }

    //@Override
    protected void codeGenListDeclVar(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        for(AbstractDeclVar var: getList()){
            var.codeGenDeclVar(compiler);
        }
    }

    protected void codeGenARMListDeclVar(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        for(AbstractDeclVar var: getList()){
            var.codeGenARMDeclVar(compiler);
        }
    }

}
