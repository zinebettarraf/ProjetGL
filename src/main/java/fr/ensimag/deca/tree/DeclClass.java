package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentTypes;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

import java.io.PrintStream;
import fr.ensimag.deca.context.ClassDefinition;
import org.mockito.stubbing.ValidableAnswer;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.StopWatch;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl23
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {

    AbstractIdentifier name;
    AbstractIdentifier superClass;
    ListDeclField declFields;
    ListDeclMethod declMethods;

    public DeclClass (AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField declFields, ListDeclMethod declMethods){
        Validate.notNull(name);
        Validate.notNull(declFields);
        Validate.notNull(declMethods);

        this.name = name;
        this.superClass = superClass;
        this.declFields = declFields;
        this.declMethods = declMethods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        if (! (superClass.getName().toString().equals("Object"))){
            s.print (" extends ");
            superClass.decompile(s);
        }
        s.println("{"); 
        s.indent();
        declFields.decompile(s);
        s.println();
        declMethods.decompile(s);
        s.unindent(); 
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
         
        // verifier l'existence de la super class 
        TypeDefinition typedef =compiler.getEnvironnementTypes().getAssociation().get(superClass.getName());
        if(typedef!= null ){  
            if(! typedef.isClass()){
                throw new ContextualError("The extended class  "+superClass.getName()+" must be of type class  ", name.getLocation());
            }
                   
            try{

                // verifier si l'ident de la classe est déjà utilisé par une classe déclarée avant
                ClassDefinition defSuper= (ClassDefinition) compiler.getEnvironnementTypes().get(superClass.getName());
                ClassType typeIdentClass=new ClassType(name.getName(), name.getLocation(), defSuper); 
                ClassDefinition defIdentClass=new ClassDefinition(typeIdentClass, name.getLocation(),defSuper);


                compiler.getEnvironnementTypes().declare(name.getName(), defIdentClass);

                int i=declFields.verifyListDeclField(compiler,defIdentClass.getMembers(), defIdentClass,defSuper.getLastIndexFields());
                defIdentClass.setLastIndexFields(i);

                int j= declMethods.verifyListDeclMethod(compiler,defIdentClass.getMembers(), defIdentClass,defSuper.getLastIndexMethods());
                defIdentClass.setLastIndexMethods(j-1);
                
                
                defIdentClass.setNumberOfFields(defIdentClass.getLastIndexFields());
                defIdentClass.setNumberOfMethods(defIdentClass.getLastIndexMethods()-defSuper.getLastIndexMethods());

                name.setDefinition(defIdentClass);
                superClass.setDefinition(defSuper);
                
            }
            catch(EnvironmentTypes.DoubleDefException e){

                     throw new ContextualError("The class "+name.getName()+" has already been declared", name.getLocation());
                     }
            }

        else {

            throw new ContextualError("The extended class  "+superClass.getName()+" does not exist", name.getLocation());
        }
       
        for(AbstractDeclMethod m: declMethods.getList()){
            m.getMethodName().getMethodDefinition().setLabel(new Label(String.format("code.%s.%s", name.getName().toString(), m.getMethodName().getName())));
        }

 }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected  void codeGenVtable(DecacCompiler compiler){
        
        RegManager manager = compiler.getRegManager();
        compiler.addComment("Vtable of class " + name.getName().toString());
        name.getClassDefinition().setDAddr(new RegisterOffset(manager.getGB(), GPRegister.GB));
        manager.incGB();
        if(name.getClassDefinition().getMembers().getParentEnvironment().getAssociation().containsKey(compiler.getSymbolTable().create("Object"))){
            compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB), Register.R0));
            

        }
        else{
            compiler.addInstruction(new LEA(superClass.getClassDefinition().getDAddr(), Register.R0));
        }
        compiler.addInstruction(new STORE(Register.R0, name.getClassDefinition().getDAddr()));
    

        int i = manager.getGB();
        for(MethodDefinition method: name.getClassDefinition().getMembers().getMethods(compiler)){
            method.setOperand(new RegisterOffset(i, GPRegister.GB));
            manager.incGB();
            compiler.addInstruction(new LOAD(new LabelOperand(method.getLabel()), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, method.getOperand()));
            i++;
        }
    }   


    @Override
    protected void codeGenMethods(DecacCompiler compiler){

        //constructor method
        RegManager manager = compiler.getRegManager();
        

        for(AbstractDeclMethod m: declMethods.getList()){
            compiler.getLabelManager().setLastFinMethod(new Label(String.format("fin.%s.%s", name.getName().toString(), m.getMethodName().getName().toString())));
            compiler.addLabel(m.getMethodName().getMethodDefinition().getLabel());
            m.codeGenDeclMethod(compiler);
        }
        

    }



    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("Not yet supported");
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        declFields.prettyPrint(s, prefix, false);
        declMethods.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        superClass.iter(f);
        declFields.iter(f);
        declMethods.iter(f);
    }

    
    @Override
    protected void codeGenDeclFields(DecacCompiler compiler){
        compiler.addComment("----------------------------------------");                    
        String elt = "--------- definition of Class : " + this.name.getName() + " ------------";
        compiler.addComment(elt);
        compiler.addComment("----------------------------------------");
        

        String labelTitle = "init." + this.name.getName();
        compiler.addLabel(new Label(labelTitle));

        if (this.name.getClassDefinition().getMembers().getParentEnvironment().getAssociation().containsKey(compiler.getSymbolTable().create("Object"))){
                for (AbstractDeclField abField : this.declFields.getList()){
                        DeclField field = (DeclField) abField;
                        field.setOnePass(true);
                        field.setToInit(true);
                        field.codeGenDeclField(compiler);
                }
        }
        else{
                //Déclarations sans initialisations

                for (AbstractDeclField abField : this.declFields.getList()){
                        DeclField field = (DeclField) abField;
                        field.setOnePass(false);
                        field.setToInit(false);
                        field.codeGenDeclField(compiler);
                }

                // Appel des champs hérités
                AbstractIdentifier current = this.superClass;
                compiler.addInstruction(new PUSH(Register.R1));
                String labelToCall = "init." + current.getName();
                compiler.addInstruction(new BSR(new Label(labelToCall)));
                compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));

                //Initialisation explicite du reste des champs... 

                for (AbstractDeclField abField : this.declFields.getList()){
                        DeclField field = (DeclField) abField;
                        AbstractInitialization init = field.getInit();
                        if(! (init instanceof NoInitialization)){
                                field.setOnePass(false);
                                field.setToInit(true);
                                field.codeGenDeclField(compiler);
                                /** 
                                GPRegister reg = init.getExpression().getReg();
                                compiler.addInstruction(new LOAD(op1, op2)); */
                        }
                }
        }

        compiler.addInstruction(new RTS());

    }


}
