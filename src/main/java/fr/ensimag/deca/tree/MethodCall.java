package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;
import fr.ensimag.deca.context.Signature;


import org.apache.commons.lang.Validate;

/**
 * Calls A method given in an argument
 *
 * @author gl23
 * @date 01/01/2022
 */
public class MethodCall extends AbstractExpr {

    private AbstractExpr leftOperand; 
    private AbstractIdentifier functionName;
    private ListExpr args;

    public MethodCall(AbstractExpr leftOperand, AbstractIdentifier functionName, ListExpr args) {
        Validate.notNull(leftOperand);
        Validate.notNull(functionName);
        Validate.notNull(args);
        
        this.leftOperand = leftOperand;
        this.functionName = functionName;
        this.args = args;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError{

            Type typeExpr=leftOperand.verifyExpr(compiler, localEnv, currentClass);
            leftOperand.setType(typeExpr);
            if(!typeExpr.isClass()){
                throw new ContextualError("The call of a method must relate to an object of type class", getLocation());
            }
            else{
                ClassDefinition defObjet = ClassDefinition.getObject();
                ClassDefinition classExpr = (ClassDefinition) compiler.getEnvironnementTypes().get(typeExpr.getName());
                ExpDefinition defSuper  =  classExpr.getMembers().get(functionName.getName());
                while( ! classExpr.getType().sameType(defObjet.getType())  && defSuper==null ){                
                    classExpr =  classExpr.getSuperClass();
                    defSuper=  classExpr.getMembers().get(functionName.getName());
                }
                if(defSuper== null){
                    throw new ContextualError("The method "+functionName.getName() +" does not belong to the selected objet", getLocation());
                }
                else{
                    MethodDefinition methodSuper=(MethodDefinition) defSuper;
                    Type returnType=functionName.verifyExpr(compiler, localEnv,classExpr);
                    Signature expectedSig= methodSuper.getSignature();
                    args.verifyExpr(compiler, localEnv, classExpr ,expectedSig);
                    
                    setType(returnType);
                    return returnType;
                }
            }

     
    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        //throw new UnsupportedOperationException("not yet implemented");
        compiler.addComment("Appel de la methode"+ functionName.getName().toString());
        
        compiler.addInstruction(new ADDSP(new ImmediateInteger(args.getList().size()+1)));

        RegManager manager = compiler.getRegManager();
        int backSP = manager.getSP();
        manager.setSP(0);
        
        GPRegister reg = getReg();
        if(reg==null){
            reg = manager.getFreeRegister();        
        }
        leftOperand.setReg(reg);
        leftOperand.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(leftOperand.getDval(), reg));
        compiler.addInstruction(new STORE(reg, new RegisterOffset(0, Register.SP)));

        
        for(AbstractExpr arg: args.getList()){
            manager.decSP();
            arg.setReg(reg);
            arg.codeGenInst(compiler);
            compiler.addInstruction(new LOAD(arg.getDval(), reg));
            compiler.addInstruction(new STORE(reg, new RegisterOffset(manager.getSP(), Register.SP)));
           
        }
        

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), reg));
        compiler.addInstruction(new CMP(new NullOperand(), reg));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));

        compiler.addInstruction(new BSR(new RegisterOffset(functionName.getMethodDefinition().getIndex(), reg)));
        
        
        compiler.addInstruction(new SUBSP(new ImmediateInteger(args.getList().size()+1)));
        

        compiler.addInstruction(new LOAD(Register.R0, reg));
        manager.setSP(backSP);
        if(getReg() == null){
            manager.freeRegister(reg);
        }
        setDval(reg);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        leftOperand.decompile(s);
        s.print(".");
        functionName.decompile(s);
        s.print("(");
        args.decompile(s);
        s.print(")");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        functionName.iter(f);
        args.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        functionName.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
    }
}
