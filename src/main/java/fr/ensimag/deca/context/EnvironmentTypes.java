package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.util.HashMap;

public class EnvironmentTypes {
    EnvironmentExp parentEnvironment;
    HashMap<Symbol,TypeDefinition> association;
    
    public EnvironmentTypes(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.association = new HashMap<Symbol,TypeDefinition>();
    }
    public HashMap<Symbol,TypeDefinition> getAssociation(){
        return this.association;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
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
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
        if(association.containsKey(name)){
            throw new DoubleDefException();
        }
        this.association.put(name, def);
    }
    
    public void preDefEvm(SymbolTable predef_table){
        
        Symbol  boolean_sym=predef_table.getMap().get("boolean");       
        this.association.put(boolean_sym,new TypeDefinition(new BooleanType(boolean_sym),Location.BUILTIN));

        Symbol  float_sym=predef_table.getMap().get("float");       
        this.association.put(float_sym,new TypeDefinition(new FloatType(float_sym),Location.BUILTIN));

        Symbol  int_sym=predef_table.getMap().get("int");       
        this.association.put(int_sym,new TypeDefinition(new IntType(int_sym),Location.BUILTIN));     
        
        Symbol  void_sym=predef_table.getMap().get("void");       
        this.association.put(void_sym,new TypeDefinition(new VoidType(void_sym),Location.BUILTIN));

       
        // ajouter la classe Objet predef
        
        Symbol  objet_sym=predef_table.getMap().get("Object"); 
        ClassType typeObjet=new ClassType(objet_sym, Location.BUILTIN, null); 
        ClassDefinition deftypeObjet=new ClassDefinition(typeObjet, Location.BUILTIN,null);
        deftypeObjet.setLastIndexFields(0);
        deftypeObjet.setLastIndexMethods(1);
        this.association.put(objet_sym,deftypeObjet);

        // evm_expr_objet pour la methode  equals 

        Signature sig= new Signature();
        sig.add(typeObjet);
        MethodDefinition defEquals =new MethodDefinition(association.get(boolean_sym).getType(),  Location.BUILTIN, sig, 1) ;
        deftypeObjet.getMembers().getAssociation().put(objet_sym,defEquals);
        defEquals.setLabel(new Label("code.Object.equals"));
        deftypeObjet.setDAddr(new RegisterOffset(1, Register.GB));
        ClassDefinition.setObject(deftypeObjet);
        


        

    }
    

}