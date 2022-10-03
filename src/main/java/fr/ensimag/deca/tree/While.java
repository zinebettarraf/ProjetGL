package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import java.nio.charset.CodingErrorAction;

import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.B;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.LDR;


/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        
        
        Label verifcond = compiler.getLabelManager().createLabel("verifcond");
        Label debutboucle = compiler.getLabelManager().createLabel("debutboucle");

        compiler.addInstruction(new BRA(verifcond));
        compiler.addLabel(debutboucle);
        body.codeGenListInst(compiler);

        compiler.addLabel(verifcond);
        condition.setReg(compiler.getRegManager().getFreeRegister());
        condition.codeGen(compiler, true, debutboucle);

        compiler.getRegManager().freeRegister(condition.getReg());


    }
    

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        Label verifcond = compiler.getLabelManager().createLabel("verifcond");
        Label debutboucle = compiler.getLabelManager().createLabel("debutboucle");

        compiler.addInstruction(new B(verifcond));
        compiler.addLabel(debutboucle);
        body.codeGenARMListInst(compiler);

        compiler.addLabel(verifcond);
        condition.setReg(compiler.getRegManager().getFreeRegister());
        if (condition instanceof Identifier) {
            compiler.addInstruction(new LDR(condition.getReg(), condition.getDval()));
            compiler.addInstruction(new LDR(condition.getReg(), new ARMRegisterOffset(condition.getReg())));
        }
        condition.codeGenARM(compiler, true, debutboucle);

        compiler.getRegManager().freeRegister(condition.getReg());
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
                this.condition.verifyCondition(compiler, localEnv, currentClass);
                for(AbstractInst inst: this.body.getList()){
                    inst.verifyInst(compiler, localEnv, currentClass, returnType);
                }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }
}
