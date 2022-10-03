package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Type TypeLeftOperand =this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                Type TypeRightOperand =this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                if(TypeLeftOperand.isClass()  || TypeRightOperand.isClass() ){
                    throw new ContextualError("Arithmetic operations are not allowed for type class ", getLocation());
                }
                this.getLeftOperand().setType(TypeLeftOperand);
                this.getRightOperand().setType(TypeRightOperand);
                if (!TypeLeftOperand.sameType(TypeRightOperand)&& ((TypeLeftOperand.isFloat() || TypeLeftOperand.isInt())) ) {
                    if (TypeLeftOperand.isFloat() && TypeRightOperand.isInt()) {
                        ConvFloat convFloat = new ConvFloat(this.getRightOperand());
                        TypeRightOperand = convFloat.verifyExpr(compiler, localEnv, currentClass);
                        convFloat.setType(TypeRightOperand);
                        this.setRightOperand(convFloat);
                        this.setType(TypeLeftOperand);
                        return TypeRightOperand;
                    } else if (TypeLeftOperand.isInt() && TypeRightOperand.isFloat()) {
                        ConvFloat convFloat = new ConvFloat(this.getLeftOperand());
                        TypeLeftOperand = convFloat.verifyExpr(compiler, localEnv, currentClass);
                        convFloat.setType(TypeLeftOperand);
                        this.setLeftOperand(convFloat);
                        this.setType(TypeLeftOperand);
                        return TypeLeftOperand;
                    } else {
                        throw new ContextualError("The two operands are not of the compatible types for the binary Operation", this.getLocation());
                    }
                }
                this.setType(TypeLeftOperand);
                return TypeLeftOperand;

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        AbstractExpr leftOperand = getLeftOperand();
        AbstractExpr rightOperand = getRightOperand();
        leftOperand.setReg(getReg());
        leftOperand.codeGenInst(compiler);
          
        GPRegister reg = getReg();
        
        compiler.addInstruction(new LOAD(leftOperand.getDval(), reg));
        
        RegManager manager = compiler.getRegManager(); 
        
        if(getRightOperand().getDval() == null){
            GPRegister scratch = reg;
            if(manager.allUsed()){
                compiler.addInstruction(new PUSH(reg));
                manager.push();
                scratch = reg;
                getRightOperand().setReg(scratch);
                getRightOperand().setDval(scratch);
                rightOperand.codeGenInst(compiler);
                GPRegister r0 = Register.getR(0);
                manager.pop(compiler, r0);
                leftOperand.setReg(r0);
                codeGen(compiler);
                compiler.addInstruction(new LOAD(r0, scratch));
                
            }
            else{
                scratch = manager.getFreeRegister();
                getRightOperand().setReg(scratch);
                getRightOperand().setDval(scratch);
                rightOperand.codeGenInst(compiler);
                codeGen(compiler);
                manager.freeRegister(scratch);
            }
        }
        else{
            codeGen(compiler);
        }
        
        rightOperand.setReg(null);
        setDval(getReg());
        
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
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
            //getRightOperand().setDval(getRightOperand().getReg());
            compiler.addInstruction(new LDR(getRightOperand().getReg(), getRightOperand().getDval()));
            compiler.addInstruction(new LDR(getRightOperand().getReg(), new ARMRegisterOffset(getRightOperand().getReg())));
            compiler.getRegManager().freeRegister(reg);
            if (getRightOperand() instanceof AbstractReadExpr) {
                compiler.getReadVars().add(getRightOperand().getDval());
                compiler.getReadVars().add(getRightOperand().getReg());
            }
            getRightOperand().setDval(getRightOperand().getReg());
        }
        codeGenARM(compiler);
        setReg(getLeftOperand().getReg());
        if (lreg != getReg()) {
            compiler.getRegManager().freeRegister(lreg);
        }
        setDval(getReg());
    }
}
