package com.agiledropzone.batchtester.tools;

public class ScenarioSyntaxException extends Exception {
    public ScenarioSyntaxException(long numLigne, String ligne, String message) {
        this("Ligne " + numLigne + " : " + message + "\n" + ligne);
    }

    public ScenarioSyntaxException(String message) {
        super("Erreur de syntaxe dans le scénario :\n" + message);
    }

    // On shunte la récupération de la stack trace
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
