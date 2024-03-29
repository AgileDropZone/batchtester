package com.agiledropzone.batchtester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiledropzone.batchtester.tools.BufferedReaderWithTimeOut;
import com.agiledropzone.batchtester.tools.ScenarioSyntaxException;
import com.agiledropzone.batchtester.tools.SimpleTimer;

/**
 * Prend en charge la lecture et le déroulement d'un scénario de test.
 */
public class ScenarioPlayer {
    private BufferedReader sceanrioReader = null;
    private String[] commandes = null;
    private final List<Action> actions = new ArrayList<Action>();
    private final List<Action> actionsLecturesDifferees = new ArrayList<Action>();
    private SimpleTimer timer = null;
    private final List<Warning> warnings = new ArrayList<Warning>();

    private class Warning {
        private final Action action;
        private final String ligneConsole;

        public Warning(Action action, String ligneConsole) {
            this.action = action;
            this.ligneConsole = ligneConsole;
        }

        public void log(PrintStream out) {
            StringBuilder sb = new StringBuilder("[").append(action.getAction()).append("] \"")
                    .append(action.getElement()).append("\" :");
            out.println(sb);
            out.println(ligneConsole);
        }
    }

    /**
     * 
     * @param fileName
     *            Fichier décrivant le scénario de test.
     * @throws ScenarioSyntaxException
     */
    public ScenarioPlayer(String fileName) throws ScenarioSyntaxException {
        traiteFichierScenario(fileName);
    }

    /**
     * Lecture d'un scénario de test.
     * 
     * @throws ScenarioSyntaxException
     */
    private void traiteFichierScenario(String fileName) throws ScenarioSyntaxException {
        String ligneATraiter = "";
        long ligneScenario = 0;
        try {
            sceanrioReader = new BufferedReader(new FileReader(fileName));

            String ligneLue;
            String ligneTmp[];
            while ((ligneLue = sceanrioReader.readLine()) != null) {
                ligneScenario++;
                ligneATraiter = ligneLue.trim();
                // On filtre les commentaires et les lignes vides
                if (ligneATraiter.startsWith("#") || ligneATraiter.length() == 0)
                    continue;

                // Identification de la commande/du script à tester
                if (ligneATraiter.startsWith("script")) {
                    // Une ligne de script doit correspondre à un format
                    // "script=xxxx xxxx"
                    if (!ligneATraiter.matches("script=.{1,}$"))
                        throw new ScenarioSyntaxException("La description du script est incorrecte ou vide");
                    ligneTmp = ligneATraiter.split("=");
                    commandes = ligneTmp[1].split(" ");
                } else {
                    // Sinon, il s'agit d'opérations du scénario à traiter dans
                    // l'ordre d'écriture dans le fichier

                    // Une action doit A MINIMA correspondre à un format
                    // "ACTION;[xxxx]"
                    // avec [xxxx] facultatif car la commande INJECT peut être
                    // vide
                    if (!ligneATraiter.matches("^.{1,};.*$"))
                        throw new ScenarioSyntaxException("La description de l'action est incorrecte ou vide");

                    actions.add(Action.createAction(ligneATraiter));
                }
            }
        } catch (ScenarioSyntaxException e) {
            throw new ScenarioSyntaxException(ligneScenario, ligneATraiter, e.getMessage());
        } catch (Exception e) {
            sceanrioReader = null;
        }

    }

    private Action getNextActionImpliqueSynchroLectureConsole(Iterator<Action> itActions, BufferedWriter output)
            throws IOException {
        Action action = null;
        Action actionBloquante = null;

        // On purge les actions de lectures différées qui n'ont pas une portée
        // globale
        for (Iterator<Action> iter = actionsLecturesDifferees.iterator(); iter.hasNext();) {
            Action actionDif = iter.next();
            if (!ActionEnum.isGlobal(actionDif.getAction())) {
                iter.remove();
            }
        }

        while (itActions.hasNext()) {
            action = itActions.next();

            if (ActionEnum.impliqueInjection(action.getAction())) {
                // Nous tentons de nous assurer (au mieux) que le process sous test peut récupérer la main avant
                // l'injection afin de garantir (ou d'optimiser les chances) sa capacité à traiter l'élément injecté.
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                output.write(action.getElement() + "\n");
                output.flush(); // Ne pas oublier le flush
                                // après l'écriture !!!
                System.out.println("[" + action.getAction() + "] " + action.getElement());

            } else if (ActionEnum.WARNING.equals(action.getAction()) || ActionEnum.ERROR.equals(action.getAction())
                    || ActionEnum.WARNING_GLOBAL.equals(action.getAction())
                    || ActionEnum.ERROR_GLOBAL.equals(action.getAction())) {
                actionsLecturesDifferees.add(action);

            } else if (ActionEnum.WAITFOR.equals(action.getAction())) {
                if (action.getDelai() > 0) {
                    timer = new SimpleTimer(Thread.currentThread(), action.getDelai(), action.getAction() + " - "
                            + action.getElement());
                    timer.start();
                }
                actionBloquante = action;
                break;
            }
        }
        return actionBloquante;
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
        BufferedReaderWithTimeOut timeOutInput = new BufferedReaderWithTimeOut(input);

        // On récupère de quoi écrire au process
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // On lit ce que nous retourne le process
        String line;

        Iterator<Action> itActions = actions.iterator();
        Action actionSynchrone = getNextActionImpliqueSynchroLectureConsole(itActions, output);

        try {
            while (actionSynchrone != null) {

                if (input.ready()) {
                    line = timeOutInput.readLine(1);
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);

                    // Nous recherchons dans un premier temps les actions
                    // synchrones
                    if (actionSynchrone.find(line)) {
                        timer.reset();

                        actionSynchrone = getNextActionImpliqueSynchroLectureConsole(itActions, output);
                    } else {
                        // puis nous recherchons les actions asynchrones
                        // ! : On se moque de l'ordre dans lequel sont testés
                        // les actions asynchrones puisque celles devant
                        // interrompre le traitement ne remonteront QUE leur
                        // erreur et les autres empilerons les messages de
                        // signalement dans l'ordre de survenue.
                        for (Action actionAsynchrone : actionsLecturesDifferees) {

                            if (actionAsynchrone.find(line)) {
                                if (actionAsynchrone.impliqueArret()) {
                                    System.err.println("\n> Le traitement fait l'objet d'une erreur :\n");
                                    new Warning(actionAsynchrone, line).log(System.err);
                                    throw new InterruptedException("Erreur");
                                } else {
                                    warnings.add(new Warning(actionAsynchrone, line));
                                }
                            }
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

        if (exitStatus == 0 && warnings.size() > 0) {
            System.out.println("\n> Le traitement fait l'objet de " + warnings.size() + " signalements :\n");
            for (Warning warning : warnings) {
                warning.log(System.out);
            }
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
