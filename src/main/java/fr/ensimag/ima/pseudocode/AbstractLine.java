package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Line of a program
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public abstract class AbstractLine {
    abstract void display(PrintStream s);
    
    private boolean armComment;
    public boolean isArmComment() {
        return armComment;
    }
    public void setArmComment(boolean armComment) {
        this.armComment = armComment;
    }
}
