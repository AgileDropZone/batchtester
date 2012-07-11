package com.agiledropzone.batchtester;

import java.io.IOException;

import com.agiledropzone.batchtester.tools.ScenarioSyntaxException;

/**
 * Classe de lancement de BatchTester. <br>
 * 
 */
public class BatchTester {

    private static String syntaxe = "Syntaxe d'appel :\njava -jar batchtester.jar <NomFichierScenario>";

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println(syntaxe);
            System.exit(1);
        }

        Thread.currentThread().setName("MAIN");

        // On récupère le scenario de test à travers un fichier spécifique
        ScenarioPlayer sReader;
        int exitStatus = -1;
        try {
            sReader = new ScenarioPlayer(args[0]);
            exitStatus = sReader.run();

            if (exitStatus == 0)
                System.out.println("Scénario OK");

        } catch (ScenarioSyntaxException e) {
            System.err.println(e.getMessage());
        }
        System.exit(exitStatus);
    }
}
