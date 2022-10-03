package fr.ensimag.ima.pseudocode;

public class ImmediateDouble extends DVal {

    private double value;

    public ImmediateDouble(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    }
    
}
