package com.agiledropzone.batchtester;

import com.agiledropzone.batchtester.tools.ScenarioSyntaxException;

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
     * @throws ScenarioSyntaxException
     */
    public static Action createAction(String description) throws ScenarioSyntaxException {
        Action action = null;
        String[] ligneTmp = description.split(";");
        ActionEnum actionScenario = ActionEnum.getInstance(ligneTmp[0]);

        if (actionScenario == null)
            throw new ScenarioSyntaxException("Aucune action n'a pu être identifiée");

        // L'action WAITFOR est particulière car elle attend 2 éléments de
        // précision :
        // le délai *et* la chaîne de caractères à attendre
        if (ActionEnum.WAITFOR.equals(actionScenario)) {

            if (!description.matches("^.{1,};[\\d]{1,};.{1,}$"))
                throw new ScenarioSyntaxException("L'action " + ActionEnum.WAITFOR.toString()
                        + " attend la spécification obligatoire d'un délai et d'une chaîne de caractères à attendre");

            action = new Action(actionScenario, ligneTmp[2]);
            action.delai = Integer.parseInt(ligneTmp[1]);
        } else {
            if (ActionEnum.INJECT.equals(actionScenario)) {
                // Cas particulier des injections pour lesquelles il faut
                // pouvoir injecter des chaines vides (un retour chariot pour
                // mettre fin à une pause)
                String element = "";
                if (ligneTmp.length > 1) {
                    element += ligneTmp[1];
                }
                action = new Action(actionScenario, element);
            } else {

                //
                if (!description.matches("^.{1,};.{1,}$"))
                    throw new ScenarioSyntaxException("L'action " + actionScenario.toString()
                            + " attend la spécification obligatoire d'une chaîne de caractères à attendre");

                action = new Action(actionScenario, ligneTmp[1]);
            }
        }
        return action;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("action [").append(this.action).append("], element [")
                .append(this.element).append("]");
        if (this.delai >= 0) {
            sb.append(", delai [").append(this.delai).append("]");
        }
        return sb.toString();
    }

    public boolean find(String consoleLine) {
        return consoleLine.contains(element);
    }

    public boolean impliqueArret() {
        return ActionEnum.ERROR.equals(action) || ActionEnum.ERROR_GLOBAL.equals(action);
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
