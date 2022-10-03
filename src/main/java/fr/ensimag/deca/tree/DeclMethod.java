package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * @author gl23
 * @date 18/01/2022
 */
public class DeclMethod extends AbstractDeclMethod {
    AbstractIdentifier type;
    AbstractIdentifier methodName;
    ListDeclParam declParam;
    AbstractMethodBody methodBody;

    public AbstractIdentifier getMethodName() {
        return methodName;
    }

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam declparam, AbstractMethodBody methodBody ){
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(declparam);
        Validate.notNull(methodBody);
        this.type = type;
        this.methodName = methodName;
        this.declParam = declparam;
        this.methodBody = methodBody;
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
    @Override
    protected boolean verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass ,int index)
            throws ContextualError {
             
            Type returnType=type.verifyType(compiler);                
            // verifier decl_param et construire la signature de la fct 
            EnvironmentExp methodEnv=new EnvironmentExp(null);
            Signature sig= declParam.verifyListDeclParam(compiler, methodEnv, currentClass);
            MethodDefinition def =new MethodDefinition(returnType, getLocation(), sig, index) ;
            def.setMethodEnv(methodEnv);
            methodName.setDefinition(def);
            // rule 2.7
            // recuperer l'ancienne def de la methode if exist
            ClassDefinition defObjet = ClassDefinition.getObject();
            ClassDefinition classParcours=currentClass;
            ExpDefinition defMethodSuper= classParcours.getMembers().get(methodName.getName());
          

            while( !classParcours.getType().sameType(defObjet.getType())  && defMethodSuper==null ){                
                classParcours=classParcours.getSuperClass();
                defMethodSuper= classParcours.getMembers().get(methodName.getName());
            }

            if(defMethodSuper!=null){
                if(defMethodSuper.isField()){
                    throw new ContextualError(" The identifier "+ methodName.getName()+" is already used for   a field in the same super  class", getLocation());
                }
                else{
                  MethodDefinition methodSuper=(MethodDefinition) defMethodSuper;
                // la methode est redefinit 
                def.setIndex(methodSuper.getIndex());  // l'index est celui de la methode à la super classe 
                if(! subType(compiler , returnType, methodSuper.getType())){
                    throw new ContextualError("The method is already defined in a super class but  with a diffrent return type", getLocation());
                }
                if(! sig.sameSignature(methodSuper.getSignature())){
                        throw new ContextualError("The method is already defined in a super class but with a diffrent signature", getLocation());
                }
                try{ 
                    currentClass.getMembers().declare(methodName.getName(), def);
                    
                    
                }catch(EnvironmentExp.DoubleDefException e){
                    throw new ContextualError(" The identifier "+ methodName.getName()+" is already used for  a  method or a field in the same class", getLocation());
                }
                methodBody.verifyMethodBody(compiler ,methodEnv , currentClass,returnType);
                return true;

                }

            }
            else{
                try{ 
                    currentClass.getMembers().declare(methodName.getName(), def);
                    
                    
                }catch(EnvironmentExp.DoubleDefException e){
                    throw new ContextualError(" The identifier "+ methodName.getName()+" is already used for  a  method or a field in the same class", getLocation());
                }
            methodBody.verifyMethodBody(compiler ,methodEnv , currentClass,returnType);
            return false;

            }
            
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("not yet implemented");
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        declParam.decompile(s);
        methodBody.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        declParam.iter(f);
        methodBody.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("not yet implemented");
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        declParam.prettyPrint(s, prefix, true);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {

        Integer insertIndex = compiler.getLastIndex();
        compiler.getRegManager().setRegCounter(1);

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));

        int index = -3;
        for(AbstractDeclParam param: declParam.getList()){
            param.setIndex(index);
            param.codeGenDeclParam(compiler); index--;
        }

        methodBody.codeGenMethodBody(compiler);


        if(!type.getDefinition().getType().isVoid()){
            Label finerreur = compiler.getLabelManager().createLabel("fin");
            compiler.addInstruction(new BRA(finerreur));
            compiler.addLabel(finerreur);
            compiler.addInstruction(new WSTR(new ImmediateString("Erreur : method "+methodName.getName().toString() + " should not return void")));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }



        compiler.addLabel(compiler.getLabelManager().getLastFinMethod());
        for (int i = compiler.getRegManager().getRegCounter(); i >= 2 ; i--) {
            compiler.addInstruction(new POP(Register.getR(i)));
        }
        compiler.addInstruction(new RTS());

        if(compiler.getRegManager().getRegCounter() != 1){
            compiler.insertInstruction(new TSTO(compiler.getRegManager().getRegCounter()-1), insertIndex);
        }
        insertIndex++;
        compiler.insertInstruction(new BOV(compiler.getPileOverflow()), insertIndex);
        insertIndex++;
        for (int i = 2; i <= compiler.getRegManager().getRegCounter(); i++) {
            compiler.insertInstruction(new PUSH(Register.getR(i)), insertIndex);
            insertIndex++;
        }

    }
}
