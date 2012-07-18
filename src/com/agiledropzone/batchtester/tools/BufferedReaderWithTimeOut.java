package com.agiledropzone.batchtester.tools;

import java.io.BufferedReader;
import java.io.IOException;

public class BufferedReaderWithTimeOut {

    /** Source de lecture */
    private BufferedReader input;

    /**
     * Buffer des éléments lus. Peut être chargé d'un appel à readLine() sur
     * l'autre, en particulier lors d'un enchainement de retours chariots.
     */
    private StringBuffer lu;
    /**
     * Indique que le buffer courant est déjà chargé et doit être retourné avant
     * toute lecture de nouveaux caractères. En général, il s'agit d'un
     * enchainement de retour chariot dont il faut reflêter la répétition.
     */
    private boolean bufferARetournerAvantLecture;

    public BufferedReaderWithTimeOut(BufferedReader input) {
        this.input = input;
        this.lu = new StringBuffer("");
        this.bufferARetournerAvantLecture = false;
    }

    /**
     * Service de lecture d'une ligne, telque BufferedReader.readLine()
     * permettant de récupérer la main sur TimeOut au cas où la ligne ne se
     * terminerait pas par un retour chariot.
     */
    public String readLine(int timeOut) throws IOException {
        StringBuffer aRetourner = new StringBuffer();

        if (this.bufferARetournerAvantLecture) {
            this.bufferARetournerAvantLecture = false;
            aRetourner.append(this.lu);
            this.lu = new StringBuffer("");
            return aRetourner.toString();
        }

        // Précaution préalable : s'il n'y a rien à lire => On rend la main tout
        // de suite.
        if (!input.ready()) {
            return null;
        }
        long timeOutAControler = timeOut * 1000;
        long timeStampDebut = System.currentTimeMillis();

        int caractereLu;

        for (;;) {
            // On commence toujours par contrôler le timeOut
            // S'il est dépassé : On retourne ce que l'on a lu jusqu'ici
            if ((System.currentTimeMillis() - timeStampDebut) >= timeOutAControler) {
                aRetourner.append(this.lu);
                this.lu = new StringBuffer("");
                return aRetourner.toString();
            }
            if (input.ready()) {
                if ((caractereLu = input.read()) > -1) {
                    // Si nous rencontrons un retour chariot, nous ne
                    // l'ajoutons pas.
                    // Nous retournons les éléments de la ligne lus
                    // jusqu'ici
                    if ((caractereLu == '\n') || (caractereLu == '\r')) {

                        aRetourner.append(this.lu);
                        this.lu = new StringBuffer("");
                        int dernierEOL = caractereLu;

                        // Un caractère de retour chariot peut en cacher un
                        // autre. S'ils sont différents, nous n'en considérons
                        // qu'un seul (cas du CRLF sous Windows ou LFCR sur de
                        // vieux systèmes).
                        if (input.ready() && (caractereLu = input.read()) > -1) {
                            if ((caractereLu == '\n') || (caractereLu == '\r')) {
                                if (caractereLu == dernierEOL) {
                                    this.bufferARetournerAvantLecture = true;
                                }
                            } else {
                                this.lu.appendCodePoint(caractereLu);
                            }
                        }

                        return aRetourner.toString();
                    }
                    this.lu.appendCodePoint(caractereLu);
                }
            }
        }
    }
}
