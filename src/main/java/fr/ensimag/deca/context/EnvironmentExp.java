package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl23
 * @date 01/01/2022
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    HashMap<Symbol,ExpDefinition> association;
    
    
    public void setParentEnvironment(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.association = new HashMap<Symbol,ExpDefinition>();
        
    }

    public ArrayList<MethodDefinition> getMethods(DecacCompiler compiler){
        ArrayList<MethodDefinition> ret = new ArrayList<MethodDefinition>();
        if(parentEnvironment != null){
            ArrayList<MethodDefinition> parentMethods = parentEnvironment.getMethods(compiler);
            ret.addAll(parentMethods);
        }
        else{
            Symbol s = compiler.getSymbolTable().getMap().get("Object");
            ClassDefinition classOb=ClassDefinition.getObject();
            MethodDefinition  def =(MethodDefinition) (classOb.getMembers().getAssociation().get(s));
            ret.add(def);
        }
        
        for(Symbol s: getAssociation().keySet()){
            ExpDefinition exp = getAssociation().get(s);
            if(exp instanceof MethodDefinition){
                MethodDefinition m = (MethodDefinition) exp;
                if(m.getIndex() > ret.size()){
                    int size = ret.size();
                    for(int i=0; i<m.getIndex()-size; i++){ ret.add(null); }
                    ret.set(m.getIndex()-1, m);
                }
                else{
                    ret.set(m.getIndex()-1, m);
                }
            }
        }


        
        return ret;
    }



    public HashMap<Symbol,ExpDefinition> getAssociation(){
        return this.association;
    }
    public EnvironmentExp getParentEnvironment(){
        return parentEnvironment;
    }


    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        //throw new UnsupportedOperationException("not yet implemented");
        if(association.containsKey(key)){
            return association.get(key);
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */

    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if(association.containsKey(name)){
            throw new DoubleDefException();
        }
        this.association.put(name, def);
    }
}
