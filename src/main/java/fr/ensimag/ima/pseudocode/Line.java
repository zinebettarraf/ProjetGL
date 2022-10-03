package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Line of code in an IMA program.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class Line extends AbstractLine {
    public Line(Label label, Instruction instruction, String comment) {
        super();
        checkComment(comment);
        this.label = label;
        this.instruction = instruction;
        this.comment = comment;
        this.setArmComment(false);
    }

    public Line(Instruction instruction) {
        super();
        this.instruction = instruction;
    }

    public Line(String comment) {
        super();
        checkComment(comment);
        this.comment = comment;
    }

    public Line(Label label) {
        super();
        this.label = label;
    }

    public Line(Directive dir) {
        super();
        this.directive = dir;
    }

    private void checkComment(final String s) {
        if (s == null) {
            return;
        }
        if (s.contains("\n")) {
            throw new IMAInternalError("Comment '" + s + "'contains newline character");
        }
        if (s.contains("\r")) {
            throw new IMAInternalError("Comment '" + s + "'contains carriage return character");
        }
    }

    private Instruction instruction;
    private String comment;
    private Label label;
    private Directive directive;

    @Override
    void display(PrintStream s) {
        boolean tab = false;
        if (label != null) {
            s.print(label);
            s.print(":");
            tab = true;
        }
        if (instruction != null) {
            s.print("\t");
            instruction.display(s);
            tab = true;
        }
        if (directive != null) {
            s.print(directive);
            tab = true;
        }
        if (comment != null) {
            if (tab) {
                            s.print("\t");
                        }
            if (isArmComment()) {
                s.print("@ " + comment);
            }
            else {
                s.print("; " + comment);
            }
        }
        s.println();
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    public Directive getDirective() {
        return directive;
    }

    public void setDirective(Directive directive) {
        this.directive = directive;
    }

}
