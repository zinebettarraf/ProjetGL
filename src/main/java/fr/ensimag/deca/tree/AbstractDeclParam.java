package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Declaration des paramètres
 *
 * @author gl23
 * @date 18/01/2022
 */
public abstract class AbstractDeclParam extends Tree {

    private int index;

    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    
    /**
     * Implements non-terminal "decl_param" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract Type verifyDeclParam(DecacCompiler compiler,
            EnvironmentExp localEnv,ClassDefinition currentClass)
            throws ContextualError;
        
    protected abstract void codeGenDeclParam(DecacCompiler compiler);
}
