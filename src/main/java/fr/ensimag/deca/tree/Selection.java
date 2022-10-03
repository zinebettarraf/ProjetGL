package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.Register;

import java.io.PrintStream;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl23
 * @date 10/01/2022
 */
public class Selection extends AbstractLValue {
    AbstractExpr leftOperand;
    AbstractIdentifier rightOperand;


    public Selection(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public AbstractIdentifier getRightOperand() {
        // The cast succeeds by construction, as the rightOperand has been set
        // as an AbstractIdentifier by the constructor.
        return (AbstractIdentifier)this.rightOperand;
    }

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            
        Type typeExpr=leftOperand.verifyExpr(compiler, localEnv, currentClass);
        leftOperand.setType(typeExpr);
        if(!typeExpr.isClass()){
            throw new ContextualError("The selection must relate to an object of type class", getLocation());
        }

        else{
            ClassDefinition defclassExpr=(ClassDefinition) compiler.getEnvironnementTypes().get(typeExpr.getName());

            ClassDefinition classParcours=defclassExpr;
            ClassDefinition defObjet = ClassDefinition.getObject();
            FieldDefinition defField = (FieldDefinition) classParcours.getMembers().get(rightOperand.getName());
            while( !classParcours.getType().sameType(defObjet.getType())  && defField ==null ){                
                classParcours=classParcours.getSuperClass();
                defField = (FieldDefinition) classParcours.getMembers().get(rightOperand.getName());               
            }
            if (defField==null){
                // parcours des parents jusqu'à objet
                throw new ContextualError("The field "+rightOperand.getName()+" is not defined for the selected objet", getLocation());
            }
            else{
            if(defField.getVisibility()== Visibility.PROTECTED){
                if(currentClass==null){
                    throw new ContextualError("The field "+rightOperand.getName()+" is declared protected and you try to get it out from  the class", getLocation());
                }
                if(!subType(compiler,currentClass.getType(),defField.getContainingClass().getType())){
                      throw new ContextualError("The field "+rightOperand.getName()+" is declared protected and you try to get it out from  the class or a subclasse", getLocation());
                }
                if(!subType(compiler,typeExpr,currentClass.getType())){
                    throw new ContextualError("The type of the expression to which the selection is applied must be a subtype of the current class", getLocation());
              }
            }
            rightOperand.setDefinition(defField);
            Type typeSelection=defField.getType();
            setType(typeSelection);
            this.isSelection(true);
            return typeSelection;

            }

        }
         
    }


    protected String getOperatorName() {
        return ".";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        GPRegister reg = this.getReg();

        if (this.getReg() == null){
            reg = compiler.getRegManager().getFreeRegister();
        }

        /** 
        if (leftOperand.getReg() == null){
            leftOperand.setReg(compiler.getRegManager().getFreeRegister());
        } */
        
        Label defNull = new Label("dereferencement.null");

        leftOperand.setReg(reg);
        leftOperand.codeGenInst(compiler);
        DVal objectAddr = leftOperand.getDval();

        Location loc = rightOperand.getExpDefinition().getLocation();
        int index;
        try {
            index = rightOperand.getExpDefinition().asFieldDefinition("This isn't a valid field", loc).getIndex();
        } catch (ContextualError e) {
            System.err.println(e.getMessage());
            index = 0;
            System.exit(1);
        }
        
        compiler.addInstruction(new LOAD(objectAddr, reg));
        compiler.addInstruction(new CMP(new NullOperand()  , reg));
        compiler.addInstruction(new BEQ(defNull));
        this.rightOperand.getExpDefinition().setOperand(new RegisterOffset(index, reg));
        this.setDval(new RegisterOffset(index, reg));
        //this.setReg(reg);

        if (this.getReg() == null){
            compiler.getRegManager().freeRegister(reg);   
        }


    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");  
    }
    
    @Override
    public void decompile (IndentPrintStream s){
        s.print("(");
        this.leftOperand.decompile(s);
        s.print(getOperatorName());
        getRightOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.leftOperand.prettyPrint(s, prefix, false);
        this.rightOperand.prettyPrint(s, prefix, false);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        this.leftOperand.iter(f);
        this.rightOperand.iter(f);
    }

    @Override
    protected DAddr getDAddr(){
        DAddr addr = (DAddr) this.getDval();
        return addr;
    }

}
