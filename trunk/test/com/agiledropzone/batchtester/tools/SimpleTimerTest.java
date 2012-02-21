package com.agiledropzone.batchtester.tools;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SimpleTimerTest {

    @Test
    public void respectDuTimeOut() throws InterruptedException {
        SimpleTimer t1 = new SimpleTimer(Thread.currentThread(), 1, "Mon travail");
        try {
            System.err.println("Je lance le timer ...");
            t1.start();
            System.err.println("Je lance le travail ...");
            Thread.sleep(700);

            t1.reset();
            assertTrue("Le time-out a été respecté", true);
        } catch (InterruptedException e) {
            t1.reset();
            fail("Le time-out aurait dû être respecté");
        }
    }

    @Test
    public void depassementDeTimeOut() {
        SimpleTimer t1 = new SimpleTimer(Thread.currentThread(), 1, "Mon travail");
        try {
            System.err.println("Je lance le timer ...");
            t1.start();
            System.err.println("Je lance le travail ...");
            Thread.sleep(5000);

            fail("Nous devrions avoir levé un TimeOut");
            t1.reset();
        } catch (InterruptedException e) {
            System.out.println("Time out intercepté");
            t1.reset();
            assertTrue("InterruptedException correctement remontée", true);
        }
    }

}
