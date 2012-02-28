package com.agiledropzone.batchtester;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class ScenarioPlayerTest {

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
