package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;


public class DeclField extends AbstractDeclField{

    private Visibility visib = Visibility.PUBLIC;
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private AbstractInitialization init;
    private boolean onePass;

    private boolean toInit;

    public AbstractInitialization getInit(){
        return this.init;
    }


    public Visibility getVisib(){
        return this.visib;
    }


    public DeclField(Visibility visib, AbstractIdentifier type, AbstractIdentifier name, AbstractInitialization init){
        this.visib = visib;
        this.type = type;
        this.name = name;
        this.init = init; 
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,int index) throws ContextualError{

            Type typeIdent=type.verifyType(compiler);
            if(typeIdent.isVoid()){
                throw new ContextualError("we can't declare a field with type void ", type.getLocation());
            }
            FieldDefinition def=new FieldDefinition(typeIdent, getLocation(), visib, currentClass, index); 
            name.setDefinition(def); 
            ClassDefinition defObjet = ClassDefinition.getObject();
            ClassDefinition classParcours=currentClass;
            ExpDefinition defSuper= classParcours.getMembers().get(name.getName());
          

            while( !classParcours.getType().sameType(defObjet.getType())  && defSuper==null ){                
                classParcours=classParcours.getSuperClass();
                defSuper= classParcours.getMembers().get(name.getName());
            }

            if(defSuper!=null){
                if(defSuper.isMethod()){
                    throw new ContextualError(" The identifier "+ name.getName()+" is already used for   a method in some super class", getLocation());
                }}
            // rule 2.5 
            try{                      
                localEnv.declare(name.getName(), def);
            }catch(EnvironmentExp.DoubleDefException e){
                throw new ContextualError(" The identifier "+ name.getName()+" is already used for  a  method or a field in the same class", getLocation()); 
            }     
            init.verifyInitialization(compiler, typeIdent, localEnv, currentClass);

    }

    void setToInit (boolean value){
        this.toInit = value;
    }

    void setOnePass (boolean value){
        this.onePass = value;
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (this.visib == Visibility.PROTECTED){
            s.print("protected ");
        }
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        s.print(" ");
        this.init.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        init.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("not yet implemented");
        
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        //throw new UnsupportedOperationException("not yet implemented");

        if(onePass){

            //Dans Ce cas, on génère le code des champs déclarés dans des classes 
            //Dont le parent est directement Object

            if (init instanceof Initialization){
                InitializedField(compiler);
            }
            else{
                int i = name.getFieldDefinition().getIndex();
                DVal defaultValue = getDefaultValue();
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1)); 
                compiler.addInstruction(new LOAD(defaultValue, Register.R0));
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(i, Register.R1)));
            }
        }        
        else if (this.toInit){   

            //Ici se fait la deuxième génération de code des champs
            //Dont la classe contenante a un parent autre que object

            InitializedField(compiler);
        }
        else{

            //Ici se fait la première génération de code des champs
            //Dont la classe contenante a un parent autre que object (i.e : avec des valeurs par défaut)

            int i = name.getFieldDefinition().getIndex();
            DVal defaultValue = getDefaultValue();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.addInstruction(new LOAD(defaultValue, Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(i, Register.R1)));
        }

    }

    public DVal getDefaultValue(){

        //La valeur par défaut d'initialisation des champs.
        
        if (type.getDefinition().getType().isFloat()){
            return new ImmediateFloat(0);
        }
        else if (type.getDefinition().getType().isInt()){
            return new ImmediateInteger(0);
        }
        else if (type.getDefinition().getType().isBoolean()){
            return new ImmediateInteger(0);
        }
        else{
            return new NullOperand();
        }
    }

    public void InitializedField (DecacCompiler compiler){

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));

        Initialization init = (Initialization) this.init;
        AbstractExpr expression = init.getExpression();

        GPRegister reg = expression.getReg();
        if(expression.getReg() == null){
            reg = compiler.getRegManager().getFreeRegister();
        }

        expression.setReg(reg);
            
        expression.codeGenInst(compiler);
        DVal dval = expression.getDval();

        if (init.getExpression() instanceof New){
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.addInstruction(new LEA(new RegisterOffset(0,(GPRegister) dval), Register.R0));
            expression.setReg(Register.R0);
        }
        else{

            if(!(dval instanceof GPRegister)){
                compiler.addInstruction(new LOAD(expression.getDval(), Register.R0));
                expression.setReg(Register.R0);
            }
            else{
                compiler.addInstruction(new LOAD((GPRegister) dval, Register.R0));
                expression.setReg(Register.R0);
            }
        }

        compiler.getRegManager().freeRegister(reg);
        

        int i = name.getFieldDefinition().getIndex();
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(i, Register.R1)));
    }

    protected void codeGenInit(DecacCompiler compiler){
        init.codeGenInit(compiler);

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), init.getExpression().getReg()));

    }
}
