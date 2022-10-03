package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Variables declaration des champs
 *
 * @author gl23
 * @date 18/01/2022
 */
public abstract class AbstractDeclField extends Tree {
    
    /**
     * Implements non-terminal "decl_field" of [SyntaxeContextuelle] in pass 3
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
    protected abstract void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass ,int index)
            throws ContextualError;
        
    protected abstract void codeGenDeclField(DecacCompiler compiler);

    protected abstract void codeGenInit(DecacCompiler compiler);
}
