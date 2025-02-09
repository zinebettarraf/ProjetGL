package fr.ensimag.deca;

import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.EnvironmentTypes;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;

import java.io.File;
import fr.ensimag.deca.context.Type;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;



/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl23
 * @date 01/01/2022
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    private SymbolTable symbolTable;
    private EnvironmentTypes types;
    private List<String> stringData;
    private List<AbstractExpr> assigns;
    private List<Operand> readVars;
    private RegManager regManager;
    private LabelManager labManager;
    private int nbRegisters;
    private Label pileOverflow;
    private Label opOverflow;
    private Label tasOverflow;
    private Label ioError;

    public SymbolTable getSymbolTable(){
        return this.symbolTable;
    }

    public EnvironmentTypes getEnvironnementTypes(){
        return this.types;
    }

    public List<String> getStringData() {
        return stringData;
    }

    public List<AbstractExpr> getAssigns() {
        return assigns;
    }

    public List<Operand> getReadVars() {
        return readVars;
    }

    public RegManager getRegManager(){
        return this.regManager;
    }
    
    public Label getPileOverflow() {
        return pileOverflow;
    }

    public Label getOpOverflow() {
        return opOverflow;
    }

    public Label getTasOverflow() {
        return tasOverflow;
    }

    public Label getIoError() {
        return ioError;
    }

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.types = new EnvironmentTypes(null);
        this.stringData = new LinkedList<String>();
        this.assigns = new ArrayList<AbstractExpr>();
        this.readVars = new ArrayList<Operand>();
        this.nbRegisters = 16;
        this.pileOverflow = new Label("pile_pleine");
        this.tasOverflow = new Label("tas_plein");
        this.opOverflow = new Label("op_overflow");
        this.ioError = new Label("io_error");


        this.symbolTable = new SymbolTable();
        symbolTable.create("boolean");
        symbolTable.create("float");
        symbolTable.create("int");
        symbolTable.create("String");
        symbolTable.create("void");
        symbolTable.create("Object");
        // remplir l' evm_types_predef
        this.types.preDefEvm(this.symbolTable);

        LabelManager labManager = new LabelManager(this);
        this.labManager = labManager;

    }

    public void setLabelManager (LabelManager labMan){
        this.labManager = labMan;
    }

    public LabelManager getLabelManager (){
        return this.labManager;
    }

    public void setRegManager (RegManager regmanager){
        this.regManager = regmanager;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Sets The number of registers used. 
     */
    public void setNbRegisters (int n){
        if (n>= 4 && n<=16){
            this.nbRegisters = n;
        }
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addARMComment(java.lang.String)
     */
    public void addARMComment(String comment) {
        program.addARMComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addDirective(java.lang.String)
     */
    public void addDirective(Directive dir) {
        program.addDirective(dir);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addARMInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addARMInstruction(Instruction instruction, String comment) {
        program.addARMInstruction(instruction, comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#getLastIndex(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public int getLastIndex() {
        return program.getLastIndex();
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#insertInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void insertInstruction(Instruction instruction, int j) {
        program.insertInstruction(instruction, j);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addFirst(Instruction instruction) {
        program.addFirst(instruction);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        String ext = "ass";
        File sourceParent = source.getParentFile();
        if (this.getCompilerOptions().isArmMode()) {
            ext = "s";
        }
        if (sourceParent != null) {
            destFile = sourceParent.getAbsolutePath() + "/" + source.getName().substring(0, source.getName().length()-4) + ext;
        }
        if (source.getParentFile() != null) {
            destFile = source.getParentFile().getAbsolutePath() + "/" + source.getName().substring(0, source.getName().length()-4) + ext;
        }
        else {
            destFile = source.getName().substring(0, source.getName().length()-4) + ext;
        }
        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */


    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }

        if (this.getCompilerOptions().getParse()) {
            prog.decompile(out);
            if (! this.getCompilerOptions().getParallel()){
                return false;
            }
        }
        
        //assert(prog.checkAllLocations());

        prog.verifyProgram(this);
        //assert(prog.checkAllDecorations());

        if (this.getCompilerOptions().getVerification()){
            if (! this.getCompilerOptions().getParallel()){
                return false;
            }
        }
        
        
        if (this.getCompilerOptions().isArmMode()) {
            addARMComment("start main program");
            prog.codeGenARMProgram(this);
            addARMComment("end main program");
        }
        else {
            addComment("start main program");
            prog.codeGenProgram(this);
            addComment("end main program");
        }
        
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");


        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;    
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

}
