package fr.ensimag.deca;

import java.io.File;
import java.lang.*;
import java.util.concurrent.Callable;

public class TaskCompile implements Callable<Boolean>{
    File source;
    CompilerOptions options;
    boolean error = false;

    public TaskCompile(File source, CompilerOptions options){
        this.source = source;
        this.options = options;
    }

    @Override
    public Boolean call(){
        DecacCompiler compiler = new DecacCompiler(options, source);
        return compiler.compile();
    }
}