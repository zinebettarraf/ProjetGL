package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.BL;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.SVC;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
/**
 * String literal
 *
 * @author gl23
 * @date 01/01/2022
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

                Type typeExpr=new StringType(compiler.getSymbolTable().create("null"));
                this.setType(typeExpr);
                return typeExpr;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    protected void codeGenARMPrint(DecacCompiler compiler) {
        int i = this.hashCode();
        String labelname;
        if (value.isBlank()) {
            labelname = "filler" + i;
            compiler.getStringData().add(labelname);
        }
        else {
            labelname = StringUtils.deleteWhitespace(value).replaceAll("[^a-zA-Z0-9]", "");
        }
        compiler.getStringData().add("\"" + value + "\"");
        compiler.addInstruction(new LDR(Register.getR(0), labelname));
        compiler.addInstruction(new BL(new Directive("printf")));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        s.print("\"");
        s.print(this.value);
        s.print("\"");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
    }

}
