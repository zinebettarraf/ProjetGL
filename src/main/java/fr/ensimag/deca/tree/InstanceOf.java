package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

import javax.swing.plaf.synth.Region;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */


public class InstanceOf extends AbstractExpr {

    AbstractExpr leftOperand;
    AbstractIdentifier rightOperand;


    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    public AbstractIdentifier getRightOperand() {
        // The cast succeeds by construction, as the rightOperand has been set
        // as an AbstractIdentifier by the constructor.
        return (AbstractIdentifier)this.rightOperand;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type leftOperandType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
                if (! (leftOperandType.isClass() || leftOperandType.isNull())){
                    throw new ContextualError("InstanceOf must be preceded by a class type  or type Null ", getLocation());
                }
                else {
                    leftOperand.setType(leftOperandType);                
                    Type typeSuper=rightOperand.verifyType(compiler); 
                    if (!typeSuper.isClass()){
                        throw new ContextualError("InstanceOf must be followed  by a class type", getLocation());
                    }                   
                    Type boolType=compiler.getEnvironnementTypes().get(compiler.getSymbolTable().create("boolean")).getType();
                    setType(boolType);
                    return boolType;

                }

    }

    protected String getOperatorName() {
        return "instanceof";
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        leftOperand.decompile(s);
        s.print(" instanceof ");
        rightOperand.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, false);

    }


    @Override
    protected void codeGen(DecacCompiler compiler, boolean branch, Label label){
        leftOperand.setReg(getReg());
        leftOperand.codeGenInst(compiler);

        Label debutboucle = compiler.getLabelManager().createLabel("debutboucle");
        Label fin = compiler.getLabelManager().createLabel("fin");



        Label success = branch? label: fin;
        Label failed = branch? fin: label;
        
        /*  
        A = a.class
        while:
            if(A is rightOperand){
                BRA success
            }
            A = A.super
            if ( A == null){
                BRA failed
            }
            BRA while
                
        */

        compiler.addInstruction(new LEA(rightOperand.getDAddr(), Register.R0));
        compiler.addInstruction(new LOAD(leftOperand.getDval(), getReg()));

        compiler.addLabel(debutboucle);
        compiler.addInstruction(new CMP((DVal)getReg(), Register.R0));
        compiler.addInstruction(new BEQ(success)); // success if A is rightOp
        compiler.addInstruction(new LOAD(new RegisterOffset(0, getReg()), getReg())); // A = A.super
        compiler.addInstruction(new CMP(new NullOperand(), getReg()));
        compiler.addInstruction(new BEQ(failed)); // failed if A == null
        compiler.addInstruction(new BRA(debutboucle));
        

        

        compiler.addLabel(fin);

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        codeGenBool(compiler, false, "result");
        setDval(getReg());
    }


}