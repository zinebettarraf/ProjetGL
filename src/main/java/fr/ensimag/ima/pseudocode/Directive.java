package fr.ensimag.ima.pseudocode;

public class Directive extends Operand {

    @Override
    public String toString() {
        return name;
    }

    public Directive(String name) {
        super();
        this.name = name;
    }
    private String name;
    
}
