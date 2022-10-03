package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        return getName().equals(otherType.getName());
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        
        ClassType typeObjet = ClassDefinition.getObject().getType();
        ClassType typeParcours=definition.getType();
        boolean result=false;
        // Null est une sous class de toute classe 
        if(definition.getType().isNull()){
               result=true;
        }
        while( !typeParcours.getName().toString().equals(typeObjet.getName().toString())){       
            if (typeParcours.sameType(potentialSuperClass)){
                result=true;
            }              
            typeParcours= typeParcours.getDefinition().getSuperClass().getType();               
        }     
        return result;
    }


}