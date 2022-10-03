package fr.ensimag.ima.pseudocode;

public class ARMRegisterOffset extends RegisterOffset {

    public ARMRegisterOffset(int offset, Register register) {
        super(offset, register);
    }
    
    public ARMRegisterOffset(Register register) {
        this(0, register);
    }

    @Override
    public String toString() {
        return "[" + this.getRegister() + ", #" + this.getOffset() + "]";
    }
}
