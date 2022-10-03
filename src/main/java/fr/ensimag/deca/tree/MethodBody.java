package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl23
 * @date 18/01/2022
 */
public class MethodBody extends AbstractMethodBody{    
    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody (ListDeclVar declVariables, ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
        
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass,Type returnType) throws ContextualError{
        declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
        insts.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        //throw new UnsupportedOperationException("not yet implemented");
        RegManager manager = compiler.getRegManager();
        int backLB = manager.getLB();
        manager.setLB(1);
        for(AbstractDeclVar declVar: declVariables.getList()){
            declVar.setLocal(true);
            declVar.codeGenDeclVar(compiler);
            manager.incLB();

        }

        insts.codeGenListInst(compiler);

        manager.setLB(backLB);

        
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.println("{"); 
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent(); 
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {

        declVariables.iter(f);
        insts.iter(f); 
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
