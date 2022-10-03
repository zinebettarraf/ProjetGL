package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type TypeLeftOperand =this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type TypeRightOperand =this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                this.getLeftOperand().setType(TypeLeftOperand);
                this.getRightOperand().setType(TypeRightOperand);
                /*if (!TypeLeftOperand.sameType(TypeRightOperand)){
                    throw new ContextualError("The two operands are not of the same type",this.getLeftOperand().getLocation());
                }*/
                if (TypeLeftOperand.isInt() || TypeLeftOperand.isFloat()){

                    if (!TypeLeftOperand.sameType(TypeRightOperand)){

                        if (TypeRightOperand.isInt() && TypeLeftOperand.isFloat()){
                            ConvFloat convFloat = new ConvFloat(this.getRightOperand()) ;
                            TypeRightOperand = convFloat.verifyExpr(compiler, localEnv, currentClass); 
                            convFloat.setType(TypeRightOperand);
                            this.setRightOperand(convFloat);
                        }

                        else if (TypeLeftOperand.isInt() && TypeRightOperand.isFloat()) {
                            ConvFloat convFloat = new ConvFloat(this.getLeftOperand()) ;
                            TypeLeftOperand = convFloat.verifyExpr(compiler, localEnv, currentClass); 
                            convFloat.setType(TypeLeftOperand);
                            this.setLeftOperand(convFloat);
                        }

                        else{
                            throw new ContextualError("Incompatible comparaison", this.getLocation());
                        }
                    }
                }
                
                Symbol  boolean_sym=compiler.getSymbolTable().getMap().get("boolean");
                Type typeExpr=compiler.getEnvironnementTypes().getAssociation().get(boolean_sym).getType();
                this.setType(typeExpr);
                return typeExpr;   

                
    }

    public abstract Instruction branchInst(boolean branch, Label label);

    public void codeGen(DecacCompiler compiler, boolean branch, Label label){
        RegManager manager = compiler.getRegManager();
        getLeftOperand().setReg(getReg());
        getLeftOperand().codeGenInst(compiler);
        compiler.addInstruction(new LOAD(getLeftOperand().getDval(), getReg()));
        if(getRightOperand().getDval() == null){
            if(!compiler.getRegManager().allUsed()){
                getRightOperand().setReg(compiler.getRegManager().getFreeRegister());
                getRightOperand().codeGenInst(compiler);
                compiler.addInstruction(new CMP(getRightOperand().getDval(), getReg()));

            }
            else{
                //throw new IllegalAccessError("pile push pop a implemente pour les booleans");
                compiler.addInstruction(new PUSH(getReg()));
                manager.push();
                GPRegister scratch = getReg();
                getRightOperand().setReg(scratch);
                getRightOperand().setDval(scratch);
                getRightOperand().codeGenInst(compiler);
                GPRegister r0 = Register.getR(0);
                manager.pop(compiler, r0);
                compiler.addInstruction(new CMP(getRightOperand().getDval(), r0));
                //compiler.addInstruction(new LOAD(r0, getReg()));;
            }
        }
        else{
            compiler.addInstruction(new CMP(getRightOperand().getDval(), getReg()));
        }
        
        compiler.addInstruction(branchInst(branch, label));

    
        compiler.getRegManager().freeRegister(getRightOperand().getReg());
    
    }

    @Override
    public void codeGenARM(DecacCompiler compiler, boolean branch, Label label) {
        //throw new UnsupportedOperationException("not yet implemented");
        GPRegister lreg;
        if (getReg() == null) {
            lreg = compiler.getRegManager().getFreeRegister();
        }
        else {
            lreg = getReg();
        }
        getLeftOperand().setReg(lreg);
        getLeftOperand().codeGenARMInst(compiler);
        if (this.getLeftOperand() instanceof Identifier || this.getLeftOperand() instanceof AbstractReadExpr) {
            compiler.addInstruction(new LDR(getLeftOperand().getReg(), getLeftOperand().getDval()));
            compiler.addInstruction(new LDR(getLeftOperand().getReg(), new ARMRegisterOffset(getLeftOperand().getReg())));
            if (getLeftOperand() instanceof AbstractReadExpr) {
                compiler.getReadVars().add(getLeftOperand().getDval());
                compiler.getReadVars().add(getLeftOperand().getReg());
            }
        }
        else {
            if (getLeftOperand().getARMDval() != getLeftOperand().getReg()) {
                compiler.addInstruction(new MOV(getLeftOperand().getReg(), getLeftOperand().getARMDval()));
            }
        }
        getRightOperand().codeGenARMInst(compiler);
        if (this.getRightOperand() instanceof Identifier || this.getRightOperand() instanceof AbstractReadExpr) {
            GPRegister reg = compiler.getRegManager().getFreeRegister();
            getRightOperand().setReg(reg);
            compiler.addInstruction(new LDR(getRightOperand().getReg(), getRightOperand().getDval()));
            compiler.addInstruction(new LDR(getRightOperand().getReg(), new ARMRegisterOffset(getRightOperand().getReg())));
            //getRightOperand().setDval(getRightOperand().getReg());
            compiler.getRegManager().freeRegister(reg);
            if (getRightOperand() instanceof AbstractReadExpr) {
                compiler.getReadVars().add(getRightOperand().getDval());
                compiler.getReadVars().add(getRightOperand().getReg());
            }
            getRightOperand().setDval(getRightOperand().getReg());
        }
        compiler.addInstruction(new CMP(getLeftOperand().getReg(), getRightOperand().getARMDval()));
        compiler.addInstruction(branchInst(branch, label));
        setReg(getLeftOperand().getReg());
        if (lreg != getReg()) {
            compiler.getRegManager().freeRegister(lreg);
        }
        setDval(getReg());
    }

    protected void codeGenInst(DecacCompiler compiler){
        codeGenBool(compiler, false, "result");
        setDval(getReg());
        
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler){
        codeGenARMBool(compiler, false, "result");
        setDval(getReg());
    }
}



