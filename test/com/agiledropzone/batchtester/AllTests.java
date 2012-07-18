package com.agiledropzone.batchtester;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.agiledropzone.batchtester.tools.BufferedReaderWithTimeOutTest;
import com.agiledropzone.batchtester.tools.SimpleTimerTest;

@RunWith(Suite.class)
@SuiteClasses({ SimpleTimerTest.class, BufferedReaderWithTimeOutTest.class, SyntaxeScenarioTest.class,
        ScenarioPlayerTest.class })
public class AllTests {

}
