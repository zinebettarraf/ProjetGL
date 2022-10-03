package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import fr.ensimag.deca.*;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse(){
        return this.parse;
    }

    public boolean getVerification(){
        return this.verification;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getNoCheck(){
        return this.noCheck;
    }

    public boolean getRegisters(){
        return this.registers;
    }

    public int getNbRegisters(){
        return this.Xregisters;
    }

    public boolean isArmMode() {
        return armMode;
    }


    private List<String> listOfOptions = List.of("-p", "-v", "-n", "-r", "-d", "-P", "-arm");
    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private boolean registers = false;
    private boolean armMode = false;
    private int Xregisters;


    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {


        // A FAIRE : parcourir args pour positionner les options correctement.

        if (args.length != 0){

            if(args[0].equals("-b")){
                if(args.length > 1){
                    throw new CLIException("No Other Options nor files can be used with the option [-b]");
                }
                this.printBanner = true;
            }
            else{
                int i = 0;
                while(listOfOptions.contains(args[i])){

                    if(args[i].equals("-p")){
                        this.parse = true;
                    }
                    else if (args[i].equals("-d")){
                        this.debug += 1;
                    }
                    else if(args[i].equals("-P")){
                        this.parallel = true;
                    }
                    else if (args[i].equals("-v")){
                        this.verification = true;
                    }
                    else if (args[i].equals("-n")){
                        this.noCheck = true;
                    }
                    else if (args[i].equals("-arm")) {
                        this.armMode = true;
                    }
                    else if (args[i].equals("-r")){
                        this.registers = true;
                        try{
                            int givenNb = Integer.parseInt(args[(i+1)]);
                            if (givenNb < 4 || givenNb > 16){
                                throw new CLIException ("The number you are giving isn't born between 4 and 16!");
                            }
                            this.Xregisters = givenNb;
                        }
                        catch(NumberFormatException e){
                            throw new CLIException("Please precise an integer right after the -r. \n it should range between 4 and 16");
                        }
                        
                        i += 1;
                    }
                    i = i+1;
                }
                for (int k=i; k<args.length; k++){
                    if (args[k].contains(".deca")){
                        sourceFiles.add(new File(args[k]));
                    }
                    else{
                        String message = "\"" + args[k] + "\"" + " is unrecognised. Please give a valid option before the name of your file, or give a deca filename";
                        throw new CLIException(message);
                    }
                    
                }
            }
        }

        if (this.verification && this.parse){
            String message = "You cannot use both -p and -v as options. Please choose One!" ;
            throw new CLIException(message);
        }
        
        
        
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        //throw new UnsupportedOperationException("not yet implemented");
        System.out.println("\nHere's the basic syntax: \ndecac [[-p|-v] [-arm | [-n] [-r X] [-d]* [-P] [-w]] <fichier deca>...] | [-b]");

    }
}
