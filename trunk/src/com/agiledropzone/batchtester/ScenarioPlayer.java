package com.agiledropzone.batchtester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiledropzone.batchtester.tools.SimpleTimer;

/**
 * Prend en charge la lecture et le déroulement d'un scénario de test.
 */
public class ScenarioPlayer {
    private BufferedReader sceanrioReader = null;
    private String[] commandes = null;
    private List<Action> actions = new ArrayList<Action>();

    /**
     * 
     * @param fileName
     *            Fichier décrivant le scénario de test.
     */
    public ScenarioPlayer(String fileName) {
        traiteFichierScenario(fileName);
    }

    /**
     * Lecture d'un scénario de test.
     */
    private void traiteFichierScenario(String fileName) {
        try {
            sceanrioReader = new BufferedReader(new FileReader(fileName));

            String ligneLue;
            String ligneATraiter;
            String ligneTmp[];
            while ((ligneLue = sceanrioReader.readLine()) != null) {
                ligneATraiter = ligneLue.trim();
                // On filtre les commentaires et les lignes vides
                if (ligneATraiter.startsWith("#") || ligneATraiter.length() == 0)
                    continue;

                // Identification de la commande/du script à tester
                if (ligneATraiter.startsWith("script")) {
                    ligneTmp = ligneATraiter.split("=");
                    commandes = ligneTmp[1].split(" ");
                } else {
                    // Sinon, il s'agit d'opérations du scénario à traiter dans
                    // l'ordre d'écriture dans le fichier
                    actions.add(Action.createAction(ligneATraiter));
                }
            }
        } catch (Exception e) {
            sceanrioReader = null;
        }

    }

    public String[] getCommande() {
        return commandes;
    }

    public List<Action> getOperations() {
        return actions;
    }

    /**
     * Déroulement du scénario de test.
     */
    public int run() throws IOException {
        int exitStatus = 0;

        ProcessBuilder launcher = new ProcessBuilder();
        launcher.redirectErrorStream(true);
        launcher.command(commandes);
        Process process;
        process = launcher.start();

        // On récupère de quoi lire ce qui sera écrit par le process
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // On récupère de quoi écrire au process
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // On lit ce que nous retourne le process
        String line;

        Iterator<Action> itOperation = actions.iterator();
        if (itOperation.hasNext()) {
            // Logiquement, la première action de test est cencée être
            // une action d'attente
            Action action = itOperation.next();
            SimpleTimer timer = null;
            if (action.getDelai() > 0) {
                timer = new SimpleTimer(Thread.currentThread(), action.getDelai(), action.getAction() + " - " + action.getElement());
                timer.start();
            }
            try {
                while (true) {
                    // Si nous avons traité l'ensemble du scénario, nous
                    // sortons
                    if (!itOperation.hasNext())
                        break;

                    if (input.ready()) {
                        line = input.readLine();
                        if (line == null) {
                            break;
                        }
                        System.out.println(line);

                        // Nous attendons que la sortie console corresponde à
                        // l'élément que nous devons attendre
                        if (ActionEnum.WAITFOR.equals(action.getAction()) && line.contains(action.getElement())) {
                            timer.reset();

                            // Nous avons identifé ce que nous attendions
                            // On récupère toutes les actions suivantes
                            // de type saisir
                            while (itOperation.hasNext()) {
                                action = itOperation.next();
                                if (!ActionEnum.INJECT.equals(action.getAction())) {
                                    if (action.getDelai() > 0) {
                                        timer = new SimpleTimer(Thread.currentThread(), action.getDelai(), action.getAction() + " - " + action.getElement());
                                        timer.start();
                                    }
                                    break;
                                }
                                output.write(action.getElement() + "\n");
                                output.flush(); // Ne pas oublier le flush
                                                // après l'écriture !!!
                                System.out.println("[INJECT] " + action.getElement());
                            }
                        }
                    } else {
                        if (Thread.currentThread().isInterrupted())
                            throw new InterruptedException("Time-out !!!");

                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                exitStatus = 1;
            }
            if (timer != null)
                timer.reset();
        }

        try {
            // Soit le process est déjà terminé
            if (process != null)
                process.exitValue();

        } catch (IllegalThreadStateException itse) {
            // Soit on l'aide à mourir
            process.destroy();
        }
        return exitStatus;
    }
}
