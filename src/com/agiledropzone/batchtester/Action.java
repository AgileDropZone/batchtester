package com.agiledropzone.batchtester;

/**
 * Action de scénario.
 */
public class Action {
    private ActionEnum action;
    private String element;
    private int delai = -1;

    public Action(ActionEnum action, String element) {
        this.action = action;
        this.element = element;
    }

    /**
     * Parse une description d'action et retourne un nouvel objet Action correspondant
     * 
     * @param description
     *            Description de l'action au format attendu dans le scenario
     */
    public static Action createAction(String description) {
        Action action = null;
        String[] ligneTmp = description.split(";");
        ActionEnum actionScenario = ActionEnum.getInstance(ligneTmp[0]);
        if (ActionEnum.WAITFOR.equals(actionScenario)) {
            action = new Action(actionScenario, ligneTmp[2]);
            action.delai = Integer.parseInt(ligneTmp[1]);
        } else if (ActionEnum.INJECT.equals(actionScenario)) {
            action = new Action(actionScenario, ligneTmp[1]);
        }
        return action;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("action [").append(this.action).append("], element [").append(this.element).append("]");
        if (this.delai >= 0) {
            sb.append(", delai [").append(this.delai).append("]");
        }
        return sb.toString();
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
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
