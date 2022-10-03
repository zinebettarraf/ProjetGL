package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentTypes;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ARMRegisterOffset;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.instructions.B;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BL;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.deca.context.ClassType;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;
    private GPRegister reg;
    private DVal dval;

    public GPRegister getReg() {
        return reg;
    }

    public void setReg(GPRegister reg) {
        this.reg = reg;
    }

    public DVal getDval() {
        return dval;
    }

    public void setDval(DVal assval) {
        this.dval = assval;
    }

    public DVal getARMDval() {
        return dval;
    }

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {

        AbstractExpr ret = this;

        Type typeExpression = this.verifyExpr(compiler, localEnv, currentClass);
        if(!assignCompatible(compiler, expectedType, typeExpression )){
            throw new ContextualError("Non compatible assignment of expression", getLocation());
        }
        else{
            if(expectedType.isFloat() && typeExpression.isInt()){
                ConvFloat convFloat = new ConvFloat(this);
                Type t = convFloat.verifyExpr(compiler, localEnv, currentClass);
                convFloat.setType(t);
                ret = convFloat;
            }
        }
        
        return ret;
        
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        if(!this.verifyExpr(compiler, localEnv, currentClass).isBoolean()){
            throw new ContextualError("Condition must be of type boolean", getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        //throw new UnsupportedOperationException("not yet implemented");
        
        this.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(getDval(), Register.getR(1))); 
        if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        }
        else if (getType().isFloat()){
            if (hex) {
                compiler.addInstruction(new WFLOATX());
            }
            else {
                compiler.addInstruction(new WFLOAT());
            }
        }
    }

    protected void codeGenARMPrint(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.codeGenARMInst(compiler);
        if (getType().isInt()) {
            compiler.addInstruction(new LDR(Register.getR(0), "printint"+Integer.MAX_VALUE)); 
            if (this instanceof Identifier || this instanceof AbstractReadExpr) {
                compiler.addInstruction(new LDR(Register.getR(1), this.getDval()));
                compiler.addInstruction(new LDR(Register.getR(1), new ARMRegisterOffset(Register.getR(1))));
                if (this instanceof AbstractReadExpr) {
                    compiler.getReadVars().add(getDval());
                    if (getReg() == null) {
                        compiler.getReadVars().add(Register.R0);
                    }
                    else {
                        compiler.getReadVars().add(getReg());
                    }
                }
            }
            else {
                compiler.addInstruction(new MOV(Register.getR(1), this.getARMDval()));
            }
        }
        if (getType().isFloat()) {
            compiler.addInstruction(new LDR(Register.getR(0), "printfloat"+Integer.MAX_VALUE));
            if (this instanceof Identifier || this instanceof AbstractReadExpr) {
                compiler.addInstruction(new LDR(Register.getR(1), this.getDval()));
                compiler.addInstruction(new LDR(Register.getR(2), new ARMRegisterOffset(Register.getR(1))));
                compiler.addInstruction(new LDR(Register.getR(3), new ARMRegisterOffset(4, Register.getR(1))));
                if (this instanceof AbstractReadExpr) {
                    compiler.getReadVars().add(getDval());
                    if (getReg() == null) {
                        compiler.getReadVars().add(Register.R0);
                    }
                    else {
                        compiler.getReadVars().add(getReg());
                    }
                }
            }
            else {
                compiler.addInstruction(new MOV(Register.getR(1), this.getARMDval()));
            }
        }
        
        compiler.addInstruction(new BL(new Directive("printf")));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");
    }    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.println(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    public boolean subType(DecacCompiler compiler , Type t1, Type t2){


        if(t1.sameType(t2) || t1.isNull()){ 
            return true;
        }
        if(t1.isClass() && t2.isClass()){

            Type typeObjet=compiler.getEnvironnementTypes().get(compiler.getSymbolTable().create("Object")).getType();
            ClassType classtype1=(ClassType)t1;
            if(t2.sameType(typeObjet) || classtype1.isSubClassOf((ClassType)t2) ){  
                return true;
            }
        }

        return false;
        
        
    }

    public boolean assignCompatible(DecacCompiler compiler, Type t1, Type t2){

 
        if(t1.isFloat() && t2.isInt()){
            return true;
        }
        
        else{
            return subType(compiler, t2, t1);
        }
    }

    public boolean castCompatible(DecacCompiler compiler , Type t1, Type t2) {


        if(assignCompatible(compiler, t1, t2) || assignCompatible(compiler, t2, t1)){
              return true;
        }
        else{
            return false;
        }
    }
    
    protected void codeGenBool(DecacCompiler compiler, boolean branch, String label) {
        LabelManager labelManager = compiler.getLabelManager();
        Label other = labelManager.createLabel(label);
        Label fin = labelManager.createLabel(label);

        codeGen(compiler, branch, other);

        compiler.addInstruction(new LOAD(new ImmediateInteger(1), getReg()));
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(other);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), getReg()));
        compiler.addLabel(fin);

    }

    protected void codeGenARMBool(DecacCompiler compiler, boolean branch, String label) {
        LabelManager labelManager = compiler.getLabelManager();
        Label other = labelManager.createLabel(label);
        Label fin = labelManager.createLabel(label);

        codeGenARM(compiler, branch, other);

        compiler.addInstruction(new MOV(getReg(), 1));
        compiler.addInstruction(new B(fin));
        compiler.addLabel(other);
        compiler.addInstruction(new MOV(getReg(), 0));
        compiler.addLabel(fin);

    }


    protected void codeGen(DecacCompiler compiler, boolean branch, Label label){
        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(getDval(), getReg()));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), getReg()));
        if(branch){
            compiler.addInstruction(new BNE(label));
        }
        else{
            compiler.addInstruction(new BEQ(label));
        }
    }

    protected void codeGenARM(DecacCompiler compiler, boolean branch, Label label){
        codeGenARMInst(compiler);
        //compiler.addInstruction(new LDR(getReg(), getDval()));
        if (this instanceof Identifier || this instanceof AbstractReadExpr) {
            compiler.addInstruction(new LDR(getReg(), getDval()));
            compiler.addInstruction(new LDR(getReg(), new ARMRegisterOffset(getReg())));
            if (this instanceof AbstractReadExpr) {
                compiler.getReadVars().add(getDval());
                compiler.getReadVars().add(getReg());
            }
        }
        compiler.addInstruction(new CMP(getReg(), new ImmediateInteger(0)));
        if(branch){
            compiler.addInstruction(new BNE(label));
        }
        else{
            compiler.addInstruction(new BEQ(label));
        }
    }

}