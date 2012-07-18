package com.agiledropzone.batchtester.tools;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

import org.junit.Test;

public class BufferedReaderWithTimeOutTest {

    @Test
    // Reconnaissance des retours chariots de type UNIX
    public void enchainementRetoursChariotsLF() throws IOException {
        // HAVING
        PipedWriter pwOut = new PipedWriter();
        PipedReader prIn = new PipedReader(pwOut);
        BufferedReaderWithTimeOut br = new BufferedReaderWithTimeOut(new BufferedReader(prIn));
        String chaineALire = "\n\n\n";
        String ligne1Attendue = "";
        String ligne2Attendue = "";
        String ligne3Attendue = "";

        // WHEN
        pwOut.write(chaineALire);

        String ligne1 = br.readLine(1);
        String ligne2 = br.readLine(1);
        String ligne3 = br.readLine(1);

        // THEN
        assertEquals("ligne1", ligne1Attendue, ligne1);
        assertEquals("ligne2", ligne2Attendue, ligne2);
        assertEquals("ligne3", ligne3Attendue, ligne3);
    }

    @Test
    // Reconnaissance des retours chariots de type WINDOWS
    public void enchainementRetoursCRLF() throws IOException {
        // HAVING
        PipedWriter pwOut = new PipedWriter();
        PipedReader prIn = new PipedReader(pwOut);
        BufferedReaderWithTimeOut br = new BufferedReaderWithTimeOut(new BufferedReader(prIn));
        String chaineALire = "\r\n\r\n\r\n";
        String ligne1Attendue = "";
        String ligne2Attendue = "";
        String ligne3Attendue = "";

        // WHEN
        pwOut.write(chaineALire);

        String ligne1 = br.readLine(1);
        String ligne2 = br.readLine(1);
        String ligne3 = br.readLine(1);

        // THEN
        assertEquals("ligne1", ligne1Attendue, ligne1);
        assertEquals("ligne2", ligne2Attendue, ligne2);
        assertEquals("ligne3", ligne3Attendue, ligne3);
    }

    @Test
    // Reconnaissance des retours chariots de type vieux MAC
    public void enchainementRetoursCR() throws IOException {
        // HAVING
        PipedWriter pwOut = new PipedWriter();
        PipedReader prIn = new PipedReader(pwOut);
        BufferedReaderWithTimeOut br = new BufferedReaderWithTimeOut(new BufferedReader(prIn));
        String chaineALire = "\r\r\r";
        String ligne1Attendue = "";
        String ligne2Attendue = "";
        String ligne3Attendue = "";

        // WHEN
        pwOut.write(chaineALire);

        String ligne1 = br.readLine(1);
        String ligne2 = br.readLine(1);
        String ligne3 = br.readLine(1);

        // THEN
        assertEquals("ligne1", ligne1Attendue, ligne1);
        assertEquals("ligne2", ligne2Attendue, ligne2);
        assertEquals("ligne3", ligne3Attendue, ligne3);
    }

    @Test
    public void lectureStandard() throws IOException {
        // HAVING
        PipedWriter pwOut = new PipedWriter();
        PipedReader prIn = new PipedReader(pwOut);
        BufferedReaderWithTimeOut br = new BufferedReaderWithTimeOut(new BufferedReader(prIn));
        String chaineALire = "Bonjour \n\nCoucou\n";
        String ligne1Attendue = "Bonjour ";
        String ligne2Attendue = "";
        String ligne3Attendue = "Coucou";

        // WHEN
        pwOut.write(chaineALire);

        String ligne1 = br.readLine(1);
        String ligne2 = br.readLine(1);
        String ligne3 = br.readLine(1);

        // THEN
        assertEquals("ligne1", ligne1Attendue, ligne1);
        assertEquals("ligne2", ligne2Attendue, ligne2);
        assertEquals("ligne3", ligne3Attendue, ligne3);
    }

    @Test
    public void lectureSansFinDeLigne() throws IOException {
        // HAVING
        PipedWriter pwOut = new PipedWriter();
        PipedReader prIn = new PipedReader(pwOut);
        BufferedReaderWithTimeOut br = new BufferedReaderWithTimeOut(new BufferedReader(prIn));
        String chaineALire = "Bonjour \n\nCoucou";
        String ligne1Attendue = "Bonjour ";
        String ligne2Attendue = "";
        String ligne3Attendue = "Coucou";

        // WHEN
        pwOut.write(chaineALire);

        String ligne1 = br.readLine(1);
        String ligne2 = br.readLine(1);
        String ligne3 = br.readLine(1);

        // THEN
        assertEquals("ligne1", ligne1Attendue, ligne1);
        assertEquals("ligne2", ligne2Attendue, ligne2);
        assertEquals("ligne3", ligne3Attendue, ligne3);
    }
}
