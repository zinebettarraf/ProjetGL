package fr.ensimag.ima.pseudocode;

public class VarCall extends DAddr {
    private String value;

    public VarCall(String value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "=" + value;
    }
    
}
