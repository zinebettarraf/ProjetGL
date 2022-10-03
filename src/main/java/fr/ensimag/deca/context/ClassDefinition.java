package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.RegisterOffset;


/**
 * Definition of a class.
 *
 * @author gl23
 * @date 01/01/2022
 */
public class ClassDefinition extends TypeDefinition {

    private DAddr addr;
    private static ClassDefinition object;

    public static void setObject(ClassDefinition object) {
        ClassDefinition.object = object;
    }


    public static ClassDefinition getObject(){
        return object;
    }


    public static void addObjectClass(DecacCompiler compiler){
        ClassDefinition object = new ClassDefinition(new ClassType(compiler.getSymbolTable().create("Object")), Location.BUILTIN, null);
        object.setDAddr(new RegisterOffset(1, Register.GB));

        Type expType = compiler.getEnvironnementTypes().get(compiler.getSymbolTable().create("boolean")).getType();
        Signature signature = new Signature(); signature.add(expType);
        MethodDefinition equals = new MethodDefinition(expType, Location.BUILTIN, signature, 1);
        equals.setLabel(new Label("code.Object.equals"));

        
        try{
            object.getMembers().declare(compiler.getSymbolTable().create("equals"), equals);
            compiler.getEnvironnementTypes().declare(compiler.getSymbolTable().create("Object"), object);
        }catch(DoubleDefException | EnvironmentTypes.DoubleDefException e){
            //?
        }
        setObject(object);
    }
    public DAddr getDAddr() {
        return addr;
    }

    public int getOffset(){
        return ((RegisterOffset) getDAddr()).getOffset();
    }

    public void setDAddr(DAddr addr) {
        this.addr = addr;
    }
    
    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }

   



    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }
    private int lastIndexMethod;
    private int lastIndexField;

    public int getLastIndexMethods() {
        return lastIndexMethod;
    }
    public int getLastIndexFields() {
        return lastIndexField;
    }
    public void setLastIndexMethods( int i) {
        this.lastIndexMethod=i;
    }
    public void setLastIndexFields(int i) {
        this.lastIndexField=i;
    }


    
}
