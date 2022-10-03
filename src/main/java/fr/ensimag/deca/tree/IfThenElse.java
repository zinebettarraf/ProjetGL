package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.B;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.MOV;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public void setElseBranch (ListInst elseBranch){
        Validate.notNull(elseBranch);
        this.elseBranch = elseBranch;
    }

    public void addToElseBranch (AbstractInst instruction){
        this.elseBranch.add(instruction);
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

                this.condition.verifyCondition(compiler, localEnv, currentClass);
                for(AbstractInst inst: this.thenBranch.getList()) {
                    inst.verifyInst(compiler, localEnv, currentClass, returnType);
                }
                for(AbstractInst inst: this.elseBranch.getList()) {
                    inst.verifyInst(compiler, localEnv, currentClass, returnType);
                }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        
        Label fin = compiler.getLabelManager().createLabel("fin");
        Label sinon = compiler.getLabelManager().createLabel("sinon");


        condition.setReg(compiler.getRegManager().getFreeRegister());
        condition.codeGen(compiler, false, sinon);
        compiler.getRegManager().freeRegister(condition.getReg());
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(fin));

        
        compiler.addLabel(sinon);
        if (!elseBranch.isEmpty()) {    
            this.elseBranch.codeGenListInst(compiler);
        }

        compiler.addLabel(fin);

        
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        Label fin = compiler.getLabelManager().createLabel("fin");
        Label sinon = compiler.getLabelManager().createLabel("sinon");


        condition.setReg(compiler.getRegManager().getFreeRegister());
        if (condition instanceof Identifier) {
            compiler.addInstruction(new LDR(condition.getReg(), condition.getDval()));
            compiler.addInstruction(new LDR(condition.getReg(), new ARMRegisterOffset(condition.getReg())));
        }
        /*else {
            compiler.addInstruction(new MOV(condition.getReg(), condition.getARMDval()));
        }*/
        condition.codeGenARM(compiler, false, sinon);
        
        compiler.getRegManager().freeRegister(condition.getReg());
        /*for(AbstractInst inst: thenBranch.getList()){
            inst.codeGenInst(compiler);
        }*/
        thenBranch.codeGenARMListInst(compiler);
        compiler.addInstruction(new B(fin));

        
        compiler.addLabel(sinon);
        if (!elseBranch.isEmpty()) {    
            this.elseBranch.codeGenARMListInst(compiler);
        }

        compiler.addLabel(fin);
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("}");
        s.println("else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.println("}");

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
