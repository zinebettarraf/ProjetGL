package fr.ensimag.deca.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;

public class RegManager {
    private int rMax;
    private int GB;
    private int SP;
    private int LB;
    private int regCounter;
    private int maxParam;
    private int maxStack;
    private ArrayList<Boolean> registers;

    private int push=0;

    public RegManager(int rMax){
        this.rMax = rMax;
        this.GB = 3;
        this.LB = 1;
        this.SP = 0;
        this.regCounter = 0;
        this.maxParam = 0;
        this.maxStack = 0;
        this.registers = new ArrayList<Boolean>();
        for(int i=0; i<rMax; i++){
            registers.add(i, false);
        }
    }
    
    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxParam() {
        return maxParam;
    }

    public void setMaxParam(int maxParam) {
        this.maxParam = maxParam;
    }


    public void setRmax (int rMax){
        this.rMax = rMax;
    }

    public int getGB(){
        return this.GB;
    }

    public void incGB(){
        GB++;
    }

    public void incLB(){
        LB++;
    }
    /** 
    public void incRegCounter(){
        regCounter++;
    } */

    public void setRegCounter(int l){
        regCounter = l;
    }

    public int getRegCounter() {
        return regCounter;
    }

    public void setLB(int l){
        LB = l;
    }

    public int getSP() {
        return SP;
    }

    public void setSP(int i) {
        SP = i;
    }

    public void decSP(){
        SP--;
    }

    public int getLB() {
        return LB;
    }
     
    public GPRegister getFreeRegister(){
        int i = 2;
        for(; i<rMax && registers.get(i) ; i++);
        // cas a traiter i == rMax: a voir
        registers.set(i, true);
        setRegCounter(Math.max(getRegCounter(), i));
        
        return Register.getR(i);
    }
    public void freeRegister(int i){
        registers.set(i, true);
    }

    public void freeRegister(GPRegister reg){
        if(reg != null){
            int i = Integer.valueOf(reg.toString().substring(1));
            registers.set(i, false);
            setRegCounter(Math.max(i, getRegCounter()));
            
        }
    }

    
    public boolean allUsed(){
        for(int i=2; i<rMax;i++){
            if(!registers.get(i)){
                return false;
            }
        }
        return true;
    } 

    

    public void push(){
        push++;
        setMaxStack(Math.max(getMaxStack(), push));
    }

    public void pop(DecacCompiler compiler, GPRegister reg){
        if(push>0){
            compiler.addInstruction(new POP(reg));
            push--;
        }
    }
}
