package com.capgemini;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.capgemini.service.ServiceTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ServiceTestSuite.class})
public class AllTests {

}
