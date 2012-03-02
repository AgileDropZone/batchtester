package com.agiledropzone.batchtester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScenarioPlayerTest {

    private static ByteArrayOutputStream outRedirect = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errRedirect = new ByteArrayOutputStream();
    private static PrintStream originalOutput = new PrintStream(System.out);

    @BeforeClass
    public static void setUpStreams() {
        System.setOut(new PrintStream(outRedirect));
        System.setErr(new PrintStream(errRedirect));
    }

    @AfterClass
    public static void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Before
    public void resetStreams() {
        outRedirect.reset();
        errRedirect.reset();
    }

    @After
    public void displayStreams() {
        String sortieStandard = outRedirect.toString();
        String sortieErreur = errRedirect.toString();
        originalOutput.print(sortieStandard);
        originalOutput.print(sortieErreur);
    }

    @Test
    public void scriptWindows_OK() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_OK <<<<<\n");

        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_OK.scenario");
        int exitStatus = sReader.run();

        assertEquals(0, exitStatus);
    }

    @Test
    public void scriptWindows_Timeout() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_Timeout <<<<<\n");

        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_KO.scenario");
        int exitStatus = sReader.run();

        assertEquals(1, exitStatus);
    }

    @Test
    public void scriptWindows_Warning() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_Warning <<<<<\n");

        // GIVEN
        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_WARNING.scenario");

        // WHEN
        int exitStatus = sReader.run();

        // THEN
        assertEquals(0, exitStatus);

        // Nous attendons 2 signalements
        String sortieStandard = outRedirect.toString();
        String signalementsAttendus = "> Le traitement fait l'objet de 2 signalements";
        assertTrue(sortieStandard.contains(signalementsAttendus));
    }

    @Test
    public void scriptWindows_Error() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_Error <<<<<\n");

        // GIVEN
        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_ERROR.scenario");

        // WHEN
        int exitStatus = sReader.run();

        // THEN
        assertEquals(1, exitStatus);

        // Nous attendons 1 erreur
        String signalementsAttendus = "> Le traitement fait l'objet d'une erreur";
        String sortieErreur = errRedirect.toString();
        assertTrue(sortieErreur.contains(signalementsAttendus));
    }

    @Test
    public void scriptWindows_Warning_Global() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_Warning_Global <<<<<\n");

        // GIVEN
        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_WARNING_GLOBAL.scenario");

        // WHEN
        int exitStatus = sReader.run();

        // THEN
        assertEquals(0, exitStatus);

        // Nous attendons 3 signalements
        String sortieStandard = outRedirect.toString();
        String signalementsAttendus = "> Le traitement fait l'objet de 3 signalements";
        assertTrue(sortieStandard.contains(signalementsAttendus));
    }

    @Test
    public void scriptWindows_Error_Global() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptWindows_Error_Global <<<<<\n");

        // GIVEN
        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_Windows_ERROR_GLOBAL.scenario");

        // WHEN
        int exitStatus = sReader.run();

        // THEN
        assertEquals(1, exitStatus);

        // Nous attendons 1 erreur
        String signalementsAttendus = "> Le traitement fait l'objet d'une erreur";
        String sortieErreur = errRedirect.toString();
        assertTrue(sortieErreur.contains(signalementsAttendus));
    }

    @Test
    public void scriptANT_OK() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptANT_OK <<<<<\n");

        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_ANT_OK.scenario");
        int exitStatus = sReader.run();

        assertEquals(0, exitStatus);
    }

    @Test
    public void scriptANT_Timeout() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : scriptANT_Timeout <<<<<\n");

        ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_script_ANT_KO.scenario");
        int exitStatus = sReader.run();

        assertEquals(1, exitStatus);
    }
}
