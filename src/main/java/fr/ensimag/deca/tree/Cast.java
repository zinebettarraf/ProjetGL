package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.INT;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Makes a Cast
 *
 * @author gl23
 * @date 10/01/2022
 */
public class Cast extends AbstractExpr {

    private AbstractIdentifier ident; 
    private AbstractExpr expr;
    public Cast(AbstractIdentifier ident, AbstractExpr exp) {
        Validate.notNull(ident);
        Validate.notNull(exp);
        
        this.ident = ident;
        this.expr = exp;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError{

            Type exprType=this.expr.verifyExpr(compiler, localEnv, currentClass);
            this.expr.setType(exprType);
            Type typeCast=this.ident.verifyType(compiler);
            this.ident.setType(typeCast);
            if(typeCast.isVoid()){
                throw new ContextualError("The type of the expression we want to  cast must not be void ", this.getLocation());
            }
            boolean compatibleTypes=castCompatible(compiler, exprType, typeCast);
            if(!compatibleTypes){
                throw new ContextualError("non compatible types for casting  ",this.getLocation());
            }
            else{
                 this.setType(typeCast);
                 return typeCast;
                }

    }

    @Override
    public void codeGenInst(DecacCompiler compiler){
        
  
        Type typeCast=ident.getDefinition().getType();
        Type typeExp=expr.getType();
        
        GPRegister reg= compiler.getRegManager().getFreeRegister();
        expr.setReg(reg);
        expr.codeGenInst(compiler);
        if(typeCast.sameType(typeExp)){
            setDval(expr.getDval());
        }
 
        else if (typeCast.isFloat() && typeExp.isInt()){
            compiler.addInstruction(new FLOAT(expr.getDval(), reg));
            setDval(reg);
        }
 
        else if (typeCast.isInt() && typeExp.isFloat()){
            compiler.addInstruction(new INT(expr.getDval(), reg));
            setDval(reg);
        }
        else if(typeExp.isNull()){
            setDval(expr.getDval());
        }
        else if (typeCast.isClass()){
            ClassType classExpr=(ClassType)typeExp;
            ClassType classCast=(ClassType)typeCast; 
            if(classExpr.isSubClassOf(classCast)){
               setDval(expr.getDval());
            }
            else{
                 // message d'erreur et arrêter le programme 
                 compiler.addInstruction(new BRA(new Label("castInvalid")));
                 setDval(reg);
            }            
        }
        compiler.getRegManager().freeRegister(reg);
         
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        ident.decompile(s);
        s.print(")");
        s.print(" (");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        ident.iter(f);
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, false);
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
    }
}