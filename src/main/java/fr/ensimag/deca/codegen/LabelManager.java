package fr.ensimag.deca.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

public class LabelManager{
    private DecacCompiler compiler;
    private HashMap <String, Label> globalLabelsMap;
    private ArrayList<String> listVerifCondLabels;
    private ArrayList<String> listDebutBoucleLabels;
    private ArrayList<String> listSinonLabels;
    private ArrayList<String> listFinLabels;
    private ArrayList<String> listEQLabels;
    private ArrayList<String> listNELabels;
    private ArrayList<String> listLTLabels;
    private ArrayList<String> listLELabels;
    private ArrayList<String> listGTLabels;
    private ArrayList<String> listGELabels;
    private ArrayList<String> listOPPLabels;
    private ArrayList<String> listSuiteLabels;
    private ArrayList<String> listResultLabels;

    private Label lastFinMethod;

    
    
    

    public Label getLastFinMethod() {
        return lastFinMethod;
    }

    public void setLastFinMethod(Label lastFinMethod) {
        this.lastFinMethod = lastFinMethod;
    }


    public LabelManager (DecacCompiler compiler){
        this.compiler = compiler;
        this.compiler.setLabelManager(this);
        this.globalLabelsMap = new HashMap <String, Label>();
        this.listVerifCondLabels = new ArrayList<String>();
        this.listDebutBoucleLabels = new ArrayList<String>();
        this.listSinonLabels = new ArrayList<String>();
        this.listFinLabels = new ArrayList<String>();
        this.listEQLabels = new ArrayList<String>();
        this.listNELabels = new ArrayList<String>();
        this.listLTLabels = new ArrayList<String>();
        this.listLELabels = new ArrayList<String>();
        this.listGTLabels = new ArrayList<String>();
        this.listGELabels = new ArrayList<String>();
        this.listOPPLabels = new ArrayList<String>();
        this.listSuiteLabels = new ArrayList<String>();
        this.listResultLabels = new ArrayList<String>();
    }

    public Label createLabel(String elt){
        int n;
        String title;
        Label newLabel;

        switch(elt){
            case "fin":
                n = listFinLabels.size() + 1;
                title = "fin" + n;
                listFinLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "verifcond":
                n = listVerifCondLabels.size() + 1;
                title = "verifcond" + n;
                listVerifCondLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "debutboucle":
                n = listDebutBoucleLabels.size() + 1;
                title = "debutboucle" + n;
                listDebutBoucleLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "sinon":
                n = listSinonLabels.size() + 1;
                title = "sinon" + n;
                listSinonLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "eq":
                n = listEQLabels.size() + 1;
                title = "eq" + n;
                listEQLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "ne":
                n = listNELabels.size() + 1;
                title = "ne" + n;
                listNELabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "lt":
                n = listLTLabels.size() + 1;
                title = "lt" + n;
                listLTLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "le":
                n = listLELabels.size() + 1;
                title = "le" + n;
                listLELabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "gt":
                n = listGTLabels.size() + 1;
                title = "gt" + n;
                listGTLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "ge":
                n = listGELabels.size() + 1;
                title = "ge" + n;
                listGELabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "opp":
                n = listOPPLabels.size() + 1;
                title = "opp" + n;
                listOPPLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "suite":
                n = listSuiteLabels.size() + 1;
                title = "suite" + n;
                listSuiteLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            case "result":
                n = listResultLabels.size() + 1;
                title = "result" + n;
                listResultLabels.add(title);
                newLabel = new Label(title);
                globalLabelsMap.put(title, newLabel);
                return newLabel;
            default:
                throw new IllegalArgumentException("You have entered a label that we haven't covered yet!!\n please add it to the Label Manager \n");    
        }
    }


}