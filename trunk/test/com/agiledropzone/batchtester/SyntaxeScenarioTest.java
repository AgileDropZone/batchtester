package com.agiledropzone.batchtester;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.agiledropzone.batchtester.tools.ScenarioSyntaxException;

public class SyntaxeScenarioTest {

    @Test
    public void ctrlSyntaxiqueScript() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueScript <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_script.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour le suivi des
            // TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 3") > 0);
            assertTrue(msg.indexOf("script=") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueWAITFORsansDelai() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueWAITFORsansDelai <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_WAITFOR1.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour le suivi des
            // TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 15") > 0);
            assertTrue(msg.indexOf("WAITFOR") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueWAITFOR() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueWAITFOR <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_WAITFOR2.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour le suivi des
            // TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 16") > 0);
            assertTrue(msg.indexOf("WAITFOR") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueWARNING() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueWARNING <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_WARNING.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour faciliter le
            // suivi des TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 18") > 0);
            assertTrue(msg.indexOf("WARNING") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueERROR() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueERROR <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_ERROR.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour faciliter le
            // suivi des TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 22") > 0);
            assertTrue(msg.indexOf("ERROR") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueWARNING_GLOBAL() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueWARNING_GLOBAL <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_WARNING_GLOBAL.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour faciliter le
            // suivi des TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 26") > 0);
            assertTrue(msg.indexOf("WARNING_GLOBAL") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueERROR_GLOBAL() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueERROR_GLOBAL <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_ERROR_GLOBAL.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour faciliter le
            // suivi des TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 29") > 0);
            assertTrue(msg.indexOf("ERROR_GLOBAL") > 0);
        }
    }

    @Test
    public void ctrlSyntaxiqueOrthographe() throws IOException {
        System.out.println("\n>>>>>  CAS DE TEST : ctrlSyntaxiqueOrthographe <<<<<\n");

        try {
            ScenarioPlayer sReader = new ScenarioPlayer("test\\resources\\Test_syntaxe_Orthographe.scenario");
            assertTrue("Nous attendons une erreur sur la syntaxe du scenario", false);
        } catch (ScenarioSyntaxException e) {
            // Sortie de l'exception attendue dans la console pour faciliter le
            // suivi des TU
            String msg = e.getMessage();
            System.out.println(msg);
            assertTrue(msg.indexOf("Ligne 31") > 0);
            assertTrue(msg.indexOf("Aucune action") > 0);
            assertTrue(msg.indexOf("ERROR_GLOBALE") > 0);
        }
    }
}
