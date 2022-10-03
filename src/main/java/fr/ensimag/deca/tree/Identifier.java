package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;

import javax.print.attribute.standard.Copies;

import org.apache.commons.lang.Validate;




/**
 * Deca Identifier
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Type getType() {
        return getExpDefinition().getType();
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                
                // l'identificateur est : var , param , indentField , identMethod

                ExpDefinition defIdent =localEnv.getAssociation().get(this.name);
                
                if (defIdent!=null){
                    // l'identificateur est un param ou var 

                    setDefinition(defIdent);
                    return defIdent.getType();

                }
                else{
                    if(currentClass==null){
                        
                        throw  new ContextualError("The variable  "+this.name+"  is not declarated yet", getLocation());
                    }
                    else{                       
                        // l'ident est un champ ou une method 
                        ExpDefinition def=currentClass.getMembers().get(name);
                        if(def==null){
                            throw  new ContextualError("The identifier  "+ name+"  is not declarated yet", getLocation());
                        }
                        else{
                            setDefinition(def);
                            return def.getType();
                        }
                    }

                }
                                
                   
    }
  
    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {

        
       if (compiler.getEnvironnementTypes().getAssociation().containsKey(this.name)){
           
           this.setDefinition(compiler.getEnvironnementTypes().getAssociation().get(this.name));
           return compiler.getEnvironnementTypes().getAssociation().get(this.name).getType();     
       }
       else {
            throw new ContextualError("The type "+this.name+"  is not authorized   " , this.getLocation());
       }      
    }

    @Override
    protected void codeGenNewId(DecacCompiler compiler, boolean isLocal){
        // variable globale
        RegManager manager = compiler.getRegManager();
        if(!isLocal){
            getExpDefinition().setOperand(new RegisterOffset(manager.getGB(), Register.GB));
        }
        else{
            getExpDefinition().setOperand(new RegisterOffset(manager.getLB(), Register.LB));
        }
        setDval(getExpDefinition().getOperand());
        manager.incGB();
    }

    @Override
    public DVal getDval() {
        return getExpDefinition().getOperand();
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler){
        if(getDefinition().isField()){
            getExpDefinition().setOperand(new RegisterOffset(getFieldDefinition().getIndex(), Register.R0));
        }
        
        else{
            setDval(getExpDefinition().getOperand());
        }
        //compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler){
        setDval(getARMDval());
        //compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
    }

    @Override
    protected DAddr getDAddr(){
        if(getDefinition().isClass()){
            return getClassDefinition().getDAddr();
        }
        return getExpDefinition().getOperand();
    }
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
    @Override
    public void codeGenPrint(DecacCompiler compiler, boolean hex) {
        // TODO Auto-generated method stub
        if (definition.isExpression()) {
            ExpDefinition defToPrint = getExpDefinition();
            compiler.addInstruction(new LOAD(defToPrint.getOperand(), Register.getR(1)));
            if (definition.getType().isInt()) {
                compiler.addInstruction(new WINT());
            }
            else if (definition.getType().isFloat()) {
                if (hex) {
                    compiler.addInstruction(new WFLOATX());
                }
                else {
                    compiler.addInstruction(new WFLOAT());
                }  
            }
        }
    }
}
