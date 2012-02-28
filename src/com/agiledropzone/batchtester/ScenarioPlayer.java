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
    private List<Operation> operations = new ArrayList<Operation>();

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
                    ligneTmp = ligneATraiter.split(";");
                    Operation op = null;
                    if ("ATTENDRE".equals(ligneTmp[0])) {
                        op = new Operation(ligneTmp[0], Integer.parseInt(ligneTmp[1]), ligneTmp[2]);
                    } else if ("SAISIR".equals(ligneTmp[0])) {
                        op = new Operation(ligneTmp[0], -1, ligneTmp[1]);
                    }
                    operations.add(op);
                }
            }
        } catch (Exception e) {
            sceanrioReader = null;
        }

    }

    public String[] getCommande() {
        return commandes;
    }

    public List<Operation> getOperations() {
        return operations;
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

        Iterator<Operation> itOperation = operations.iterator();
        if (itOperation.hasNext()) {
            // Logiquement, la première opération de test est cencée être
            // une opération d'attente
            Operation op = itOperation.next();
            SimpleTimer timer = null;
            if (op.getDelai() > 0) {
                timer = new SimpleTimer(Thread.currentThread(), op.getDelai(), op.getOperation() + " - "
                        + op.getElement());
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
                        if ("ATTENDRE".equals(op.getOperation()) && line.contains(op.getElement())) {
                            timer.reset();

                            // Nous avons identifé ce que nous attendions
                            // On récupère toutes les opérations suivantes
                            // de type saisir
                            while (itOperation.hasNext()) {
                                op = itOperation.next();
                                if (!"SAISIR".equals(op.getOperation())) {
                                    if (op.getDelai() > 0) {
                                        timer = new SimpleTimer(Thread.currentThread(), op.getDelai(),
                                                op.getOperation() + " - " + op.getElement());
                                        timer.start();
                                    }
                                    break;
                                }
                                output.write(op.getElement() + "\n");
                                output.flush(); // Ne pas oublier le flush
                                                // après l'écriture !!!
                                System.out.println("[SAISIE] " + op.getElement());
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
