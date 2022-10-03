package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl23
 * @date 01/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;


    
    
    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
          // LOG.debug("verify program: start");
         // LOG.debug("verify program: end");
        //ClassDefinition.addObjectClass(compiler);
        
        this.classes.verifyListClass(compiler);
        this.main.verifyMain(compiler);
    }



    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code

        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, GPRegister.GB)));
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, GPRegister.GB)));

        for(AbstractDeclClass declClass: classes.getList()){
            declClass.codeGenVtable(compiler);
        }


        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.addComment("Error messages : stack");
        compiler.addLabel(compiler.getPileOverflow());
        compiler.addInstruction(new WSTR("Erreur: overflow de la pile"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addComment("Error messages : operations");
        compiler.addLabel(compiler.getOpOverflow());
        compiler.addInstruction(new WSTR("Erreur: overflow pendant le calcul"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addLabel(compiler.getIoError());
        compiler.addInstruction(new WSTR("Error; Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addLabel(new Label("dereferencement.null"));
        compiler.addInstruction(new WSTR("Erreur : dereferencement null"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addLabel(new Label("castInvalid"));
        compiler.addInstruction(new WSTR("Erreur : Invalid Cast "));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addLabel(new Label("tas_plein"));
        compiler.addInstruction(new WSTR("Erreur : allocation impossible, tas plein"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addInstruction(new WSTR(new ImmediateString("Erreur : dereferencement de null")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());


        //class definitions
        compiler.addComment("Classes Definitions"); 

        //class Object
        defObject(compiler);

        for(AbstractDeclClass declClass: classes.getList()){
            declClass.codeGenDeclFields(compiler);
            declClass.codeGenMethods(compiler);
        }
        
    }

    public void defObject(DecacCompiler compiler){
        compiler.addLabel(new Label("init.Object"));
        compiler.addInstruction(new RTS());

        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new TSTO(1));
        compiler.addInstruction(new BOV(compiler.getPileOverflow()));
        compiler.addInstruction(new PUSH(Register.getR(2)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(2)));
        compiler.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.getR(2)));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new RTS());


    }

    public void codeGenARMProgram(DecacCompiler compiler) {        
        compiler.addDirective(new Directive(".arch armv8-a"));
        compiler.addDirective(new Directive(".section .text"));
        compiler.addDirective(new Directive(".extern printf")); 
        compiler.addDirective(new Directive(".extern scanf")); 
        compiler.addDirective(new Directive(".global main"));
        compiler.addLabel(new Label("main"));
        main.codeGenARMMain(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
