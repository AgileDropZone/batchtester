package com.agiledropzone.batchtester;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SyntaxeScenarioTest.class, ScenarioPlayerTest.class })
public class AllTests {

}
