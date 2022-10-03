package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * 
 *
 * @author gl23
 * @date 18/01/2022
 */
public abstract class AbstractMethodBody extends Tree {


    abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass, Type returnType)throws ContextualError;
    abstract void codeGenMethodBody(DecacCompiler compiler);
}
