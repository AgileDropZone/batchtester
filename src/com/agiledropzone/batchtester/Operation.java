package com.agiledropzone.batchtester;

/**
 * Opération de scénario.
 */
public class Operation {
    private String operation;
    private int delai;
    private String element;

    public Operation(String operation, int delai, String element) {
        this.operation = operation;
        this.delai = delai;
        this.element = element;
    }

    @Override
    public String toString() {
        return "operation [" + this.operation + "], delai [" + this.delai + "], element [" + this.element + "]";
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getDelai() {
        return delai;
    }

    public void setDelai(int delai) {
        this.delai = delai;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
}
