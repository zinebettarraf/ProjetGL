package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MOV;
import fr.ensimag.ima.pseudocode.instructions.SVC;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongBiFunction;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;

/**
 * @author gl23
 * @date 01/01/2022
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    private List<String> labels;

    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
        this.labels = new ArrayList<String>();
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify Main: start");
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        //LOG.debug("verify Main: end");
        //throw new UnsupportedOperationException("not yet implemented");
        EnvironmentExp localenv = new EnvironmentExp(null);
        this.declVariables.verifyListDeclVariable(compiler, localenv, null);
        this.insts.verifyListInst(compiler, localenv, null, new VoidType(compiler.getSymbolTable().create("void")));


    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        compiler.addComment("Variables declarations:");
        declVariables.codeGenListDeclVar(compiler);
        
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);

        int pileCheck = compiler.getRegManager().getGB() - 1 + compiler.getRegManager().getMaxStack();
        compiler.addFirst(new ADDSP(pileCheck));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addFirst(new BOV(compiler.getPileOverflow()));
        }
        
        if (compiler.getRegManager().getMaxParam() != 0) {
            pileCheck += 2 + compiler.getRegManager().getMaxParam();
        }
        compiler.addFirst(new TSTO(pileCheck)); 
    }

    protected void codeGenARMMain(DecacCompiler compiler) {
        declVariables.codeGenARMListDeclVar(compiler);
        compiler.addARMComment("Beginning of main instructions:");
        insts.codeGenARMListInst(compiler);
        //exit (halt)
        compiler.addInstruction(new MOV(Register.getR(7), new ImmediateInteger(1)));
        compiler.addInstruction(new SVC(0));
        compiler.addARMComment("End of main instructions:");
        //data
        compiler.addDirective(new Directive(".data"));
        addARMData(compiler);
    }

    /**
     * Writes the .data section in the generated arm assembly file
     * 
     * @author gl23
     * @date 11/02/2022
     */
    public void addARMData(DecacCompiler compiler) {
        String labelname;
        for (String s : compiler.getStringData()) {
            if (s.substring(1,s.length()-1).isBlank()) {
                continue;
            }
            labelname = StringUtils.deleteWhitespace(s.substring(1,s.length()-1)).replaceAll("[^a-zA-Z0-9]", "");
            if (s.length()>=6) {
                if (s.substring(0, 6).equals("filler")) {
                    labelname = new String(s);
                    s = compiler.getStringData().get(compiler.getStringData().indexOf(s) + 1);
                }
            }
            if (labels.contains(labelname)) {
                continue;
            }
            compiler.addDirective(new Directive(".align"));
            compiler.addLabel(new Label(labelname));
            compiler.addDirective(new Directive(".asciz " + s));
            compiler.addDirective(new Directive("len = .-" + labelname));
            labels.add(labelname);
        }
        compiler.addDirective(new Directive(".align"));
        compiler.addLabel(new Label("printint"+Integer.MAX_VALUE));
        compiler.addDirective(new Directive(".asciz " + "\"" + "%i" + "\""));
        compiler.addDirective(new Directive(".align"));
        compiler.addLabel(new Label("printfloat"+Integer.MAX_VALUE));
        compiler.addDirective(new Directive(".asciz " + "\"" + "%f" + "\""));
        compiler.addDirective(new Directive(".align"));
        compiler.addLabel(new Label("scanner"+Integer.MAX_VALUE));
        compiler.addDirective(new Directive(".asciz " + "\"" + "%i" + "\""));

        compiler.addARMComment("Variables declarations:");
        int i = 0;
        for (AbstractDeclVar var : declVariables.getList()) {
            String to_label = var.getVarName().getDval().toString().substring(1);
            String val;
            String mode = ".word ";
            if (var.getVarName().getType().isFloat()) {
                mode = ".double ";
            }
            if (var.getInitialization().getExpression() != null) {
                val = var.getVarName().getARMDval().toString().substring(1);
            }
            else {
                val = "1";
                if (!compiler.getAssigns().isEmpty() && compiler.getAssigns().get(i) != null) {
                    val = compiler.getAssigns().get(i).getARMDval().toString().substring(1);
                }   
                i += 1;
            }
            compiler.addLabel(new Label(to_label));
            compiler.addDirective(new Directive(mode + val));
        }

        compiler.addARMComment("Read Variables declarations:");
        for (Operand expr : compiler.getReadVars()) {
            if (expr.toString().length()<3) {
                continue;
            }
            compiler.addLabel(new Label(expr.toString().substring(1)));
            compiler.addDirective(new Directive(".word " + compiler.getReadVars().get(compiler.getReadVars().indexOf(expr)+1).toString().substring(1)));
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{"); 
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent(); 
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
