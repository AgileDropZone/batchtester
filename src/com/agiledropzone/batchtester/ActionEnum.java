package com.agiledropzone.batchtester;

import java.util.Arrays;
import java.util.List;

/**
 * Enum des actions de scénario gérées.
 */
public enum ActionEnum {
    WAITFOR("WAITFOR", "ATTENDRE"), INJECT("INJECT", "SAISIR"), WARNING("WARNING"), ERROR("ERROR"), WARNING_GLOBAL(
            "WARNING_GLOBAL"), ERROR_GLOBAL("ERROR_GLOBAL");
    private final List<String> values;

    private ActionEnum(String... values) {
        this.values = Arrays.asList(values);
    }

    private List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.get(0);
    }

    public static ActionEnum getInstance(String value) {
        for (ActionEnum action : ActionEnum.values()) {
            if (action.getValues().contains(value)) {
                return action;
            }
        }
        return null;
    }

    public static boolean impliqueInjection(ActionEnum actionEnum) {
        return actionEnum.equals(INJECT);
    }

    public static boolean isGlobal(ActionEnum actionEnum) {
        return actionEnum.equals(WARNING_GLOBAL) || actionEnum.equals(ERROR_GLOBAL);
    }
}
