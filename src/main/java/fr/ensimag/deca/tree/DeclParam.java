package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.context.ParamDefinition;
import java.io.PrintStream;
import java.security.GeneralSecurityException;

import org.apache.commons.lang.Validate;

/**
 * @author gl23
 * @date 18/01/2022
 */
public class DeclParam extends AbstractDeclParam {
    AbstractIdentifier type;
    AbstractIdentifier name;

    


    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
    }


    @Override
    protected Type verifyDeclParam(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
               Type typeParam=type.verifyType(compiler);
               if (typeParam.isVoid()){
                throw new ContextualError("The type of an argument in the input of a function must not be Void ", type.getLocation());
               }
               ParamDefinition paramDef=new ParamDefinition(typeParam,getLocation());
               try{ 
                localEnv.declare(name.getName(), paramDef);
                
              }catch(EnvironmentExp.DoubleDefException e){
                throw new ContextualError("Arguments in a function must be different ", getLocation());
               }
               name.setDefinition(paramDef);
               return typeParam;
            }

    
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("not yet implemented");
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
    }

    @Override
    protected void codeGenDeclParam(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        name.getExpDefinition().setOperand(new RegisterOffset(getIndex(), Register.LB));
    }
}
