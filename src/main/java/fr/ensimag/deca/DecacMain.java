package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.TaskCompile;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.lang.runtime.*;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args); 
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            //throw new UnsupportedOperationException("decac -b not yet implemented");
            System.out.println("Groupe gl23 - Groupe 5");
        }
        else if (options.getSourceFiles().isEmpty()) {
            //throw new UnsupportedOperationException("decac without argument not yet implemented");
            String text = "decac [[-p|-v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b] \n \n" ;
            text += ". -b (banner) : affiche une bannière indiquant le nom de l’équipe \n \n";
            text += ". -p (parse) : arrête decac après l'étape de construction de l'arbre, et affiche la décompilation de ce dernier \n";
            text += "       i.e. s'l n'y a qu'un fichier source à compiler, la sortie doit être un programme deca syntaxiquement correct)\n \n";
            text +=  ". -n (no check) : supprime les tests à l’exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca. \n \n"; 
            text += ". -r X (registers) : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16 \n \n";
            text += ". -d (debug) : active les traces de debug. Répéter l’option plusieurs fois pour avoir plus de traces. \n \n";
            text += ". -P (parallel) : s’il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation) \n ";
            text += ". -arm (ARM compilation mode) : compile vers assembleur ARM 32-bits";

            System.out.println(text);

        }
        if (options.getParallel() && !(options.getParse() || options.getVerification())) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            //throw new UnsupportedOperationException("Parallel build not yet implemented");


            int n = Runtime.getRuntime().availableProcessors();
            //ThreadFactory newFact = Executors.defaultThreadFactory();
            ExecutorService exc = Executors.newFixedThreadPool(n*7);
            Set<Future<Boolean>> newSet = new HashSet<Future<Boolean>>();
            for (File source : options.getSourceFiles()){
                TaskCompile newTask = new TaskCompile(source, options);
                Future<Boolean> element = exc.submit(newTask);
                newSet.add(element);
            }

            for (Future<Boolean> elt : newSet){
                try {
                    elt.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println(e.getMessage());
                }
            }

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if(options.getRegisters()){
                    int n = options.getNbRegisters();
                    compiler.setNbRegisters(n);
                    
                    compiler.setRegManager(new RegManager(n));
                    compiler.getRegManager().setRmax(n);
                }
                else {
                    compiler.setRegManager(new RegManager(16));
                }
                if (options.isArmMode()) {
                    System.out.println("Compiling in ARM MODE \n");
                }
                if (compiler.compile()) {
                    error = true;
                }
            }  
        }
        System.exit(error ? 1 : 0);
    }
}
