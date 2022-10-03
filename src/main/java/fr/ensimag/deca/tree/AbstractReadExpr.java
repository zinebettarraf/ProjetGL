package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Directive;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.VarCall;
import fr.ensimag.ima.pseudocode.instructions.BL;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LDR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl23
 * @date 01/01/2022
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addInstruction(new BOV(compiler.getIoError()));
        }
        compiler.addInstruction(new LOAD(Register.getR(1), getReg()));
        setDval(getReg());
    }

    @Override
    protected void codeGenARMInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        String read = "read"+hashCode();
        compiler.addInstruction(new LDR(Register.getR(0), "scanner"+Integer.MAX_VALUE));
        compiler.addInstruction(new LDR(Register.getR(1), read));
        compiler.addInstruction(new BL(new Directive("scanf")));
        setDval(new VarCall(read));
    }
}
