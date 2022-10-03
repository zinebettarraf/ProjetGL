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
 * Print statement (print, println, ...).
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        for(AbstractExpr expr: arguments.getList()){
            Type expType = expr.verifyExpr(compiler, localEnv, currentClass);
            if(!(expType.isInt() || expType.isFloat() || expType.isString())){
                throw new ContextualError("type of argument must be either int, float or String", getLocation());
            } 

        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.setReg(compiler.getRegManager().getFreeRegister());
            if (getPrintHex()) {
                a.codeGenPrint(compiler, true);
            }
            else {
                a.codeGenPrint(compiler, false);
            }
            compiler.getRegManager().freeRegister(a.getReg());
            a.setReg(null);
        }
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenARMPrint(compiler);
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.getSuffix().equals("ln")){
            if(getPrintHex()){
                s.print("printlnx(");
                arguments.decompile(s);
                s.println(");");
            }
            else{
                s.print("println(");
                this.arguments.decompile(s);
                s.println(");");
            }
        }
        else{
            if(getPrintHex()){
                s.print("printx(");
                arguments.decompile(s);
                s.println(");");
            }
            else{
                s.print("print(");
                this.arguments.decompile(s);
                s.println(");");
            }
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
