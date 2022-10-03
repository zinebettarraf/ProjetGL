package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import net.bytebuddy.asm.AsmVisitorWrapper;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractInitialization extends Tree {

    public abstract AbstractExpr getExpression();
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void codeGenInit(DecacCompiler compiler);

    protected abstract void codeGenARMInit(DecacCompiler compiler);
}
