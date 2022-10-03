package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.VarCall;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.STR;

import java.io.PrintStream;
import java.security.GeneralSecurityException;

import org.apache.commons.lang.Validate;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    public AbstractIdentifier getVarName() {
        return varName;
    }

    public AbstractInitialization getInitialization() {
        return initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
                Type typeIdent=type.verifyType(compiler);
                if(typeIdent.isVoid()){
                        throw new ContextualError("we can't declare an identifier with type void ", type.getLocation());
                    }
                VariableDefinition def = new VariableDefinition( typeIdent, varName.getLocation());
                try{ 
                    localEnv.declare(varName.getName(), def);                
                }catch(EnvironmentExp.DoubleDefException e){
                    throw new ContextualError("Redeclaration of the variable  " + varName.getName(), varName.getLocation());
                }
                this.initialization.verifyInitialization(compiler,typeIdent, localEnv, currentClass);
                varName.setDefinition(def);
             

    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        s.print(" ");
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        varName.codeGenNewId(compiler, isLocal());
        initialization.codeGenInit(compiler);
        if(initialization.getExpression() != null){
            GPRegister reg = initialization.getExpression().getReg();
            DAddr addr = varName.getVariableDefinition().getOperand();
            compiler.addInstruction(new STORE(reg, addr));

            //varName.setDval(addr);
            
            compiler.getRegManager().freeRegister(reg);
            initialization.getExpression().setReg(null);
            
        }
           
    }

    @Override
    protected void codeGenARMDeclVar(DecacCompiler compiler) {
        varName.getExpDefinition().setOperand(new VarCall(varName.getName().toString() + Integer.MAX_VALUE));
        initialization.codeGenARMInit(compiler);
        if (initialization.getExpression() instanceof AbstractReadExpr) {
            varName.getExpDefinition().setOperand((VarCall) initialization.getExpression().getARMDval());
        }
        if(initialization.getExpression() != null){
            varName.setDval(initialization.getExpression().getARMDval());
            if (initialization.getExpression().getReg() != null) {
                GPRegister reg = compiler.getRegManager().getFreeRegister();
                compiler.addInstruction(new LDR(reg, varName.getDval()));
                compiler.addInstruction(new STR(initialization.getExpression().getReg(), new ARMRegisterOffset(reg)));
                compiler.getRegManager().freeRegister(reg);
                compiler.getRegManager().freeRegister(initialization.getExpression().getReg());
            }
        }
    }
}
