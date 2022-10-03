package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Creates a new Element
 *
 * @author gl23
 * @date 10/01/2022
 */
public class New extends AbstractExpr {

    private AbstractIdentifier ident; 

    public New(AbstractIdentifier ident) {
        Validate.notNull(ident);
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

            Type typeNew =ident.verifyType(compiler);
            if (!typeNew.isClass()){
                throw new ContextualError("New must be followed by a class Type " ,getLocation());
            }
            setType(typeNew);
            return typeNew;

    }

    @Override
    public void codeGenInst(DecacCompiler compiler){

        ClassDefinition def =(ClassDefinition) ident.getDefinition();  
        int numberOfFields=def.getNumberOfFields();
        GPRegister regDes=getReg();
        boolean free=false;
        if(regDes==null){
            regDes=compiler.getRegManager().getFreeRegister();
            setReg(regDes);
            free=true;
        }    
        setDval(getReg());
        compiler.addInstruction(new NEW(new ImmediateInteger(numberOfFields+1), getReg()));
        compiler.addInstruction(new BOV(new Label("tas_plein"))); //// 
        compiler.addInstruction(new LEA(def.getDAddr() , Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0, getReg())));
        compiler.addInstruction(new PUSH(getReg()));
        compiler.addInstruction(new BSR(new Label(String.format("init.%s",def.getType().getName().toString()))));
        compiler.addInstruction(new POP(getReg()));
        if(free){
            compiler.getRegManager().freeRegister(regDes);
        }

        



    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        ident.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
    }
}