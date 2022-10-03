package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl23
 * @date 18/01/2022
 */
public class MethodAsmBody extends AbstractMethodBody{
    StringLiteral code;

    public MethodAsmBody (StringLiteral code) {
        Validate.notNull(code);
        this.code = code;
        
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass, Type returnType) throws ContextualError{

    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        //throw new UnsupportedOperationException("not yet implemented");
        compiler.add(new InlinePortion(code.getValue()));
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.println(" asm( ");
        s.indent();
        code.decompile(s);
        s.println();
        s.unindent();
        s.println(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {

        code.iter(f);
         
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        code.prettyPrint(s, prefix, true);
    }
}
