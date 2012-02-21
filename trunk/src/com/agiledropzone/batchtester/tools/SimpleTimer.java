package com.agiledropzone.batchtester.tools;

/**
 * Classe simpliste de gestion d'un timer.<br>
 * Si le timer a été lancé, soit il est réinitialisé par un appel à reset(),
 * soit la méthode timeout() est invoqué.<br>
 * Dans un tel cas, le thread appelant est convié par l'appel à interrupt().<br>
 * L'appelant doit donc considérer l'opportunité de tester régulièrement
 * Thread.currentThread().isInterrupted().<br>
 * <br>
 * La durée du timer est exprimé ici en secondes.
 */
public class SimpleTimer extends Thread {
    /** Fréquence de contrôle du timeout */
    protected int rate = 100;

    /**
     * Thread auquel nous allons transmettre le time-out. Volatile : pour
     * assurer la coordination des threads
     */
    private volatile Thread parentThread;

    /** Durée du timeout */
    private int length;

    /** Temps écoulé depuis le démarrage du timer */
    private int elapsedTime;

    /** Opération soumise au timer */
    private String operation;

    /**
     * @param parentThread
     *            Thread soumis au timer
     * @param length
     *            Durée du timer (en secondes !!!)
     * @param operation
     *            Libellé identifiant l'opération en cours soumise au timer
     */
    public SimpleTimer(Thread parentThread, int length, String operation) {
        this.parentThread = parentThread;
        this.length = length * 1000;
        this.operation = operation;

        this.elapsedTime = 0;

        this.setDaemon(true);
        this.setName(operation);
    }

    /** Réinitialisation du timer */
    public synchronized void reset() {
        elapsedTime = -1;
    }

    /**
     * Lancement du timer <br>
     * Cette méthode est invoquée par timer.start().
     */
    @Override
    public void run() {
        // On boucle jusqu'à l'écoulement du timer ou son intérruption
        // volontaire
        for (;;) {
            // Mise en sommeil du timer
            try {
                Thread.sleep(rate);
            } catch (InterruptedException ioe) {
                continue;
            }

            // La lecture et mise à jour de elapsedTime est soumises à une
            // synchro
            synchronized (this) {
                if (elapsedTime == -1)
                    break;
                elapsedTime += rate;
                if (elapsedTime > length) {
                    timeout();
                    break;
                }
            }
        }
    }

    /**
     * Déclenchement de l'opération attendu sur le timeout.<br>
     * Ici, nous invitons poliment le thread appelant à s'interrompre à travers
     * un appel à interrupt().
     */
    public void timeout() {
        System.err.println("Time-out sur [" + operation + "]");
        parentThread.interrupt();
    }

}