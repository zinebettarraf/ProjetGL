package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl23
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        //throw new UnsupportedOperationException("not yet implemented");
        this.setLocation(getOperand().getLocation());
        Type typeExpr=new FloatType((compiler.getSymbolTable().create("float")));
        this.setType(typeExpr);
        return typeExpr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler){
        AbstractExpr operand = getOperand();

        operand.setReg(getReg());

        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new FLOAT(getOperand().getDval(), getReg()));
        setDval(getReg());


    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
}
