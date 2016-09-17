package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.JSU_001_001;

/**
 * Testsuite des UseCase Login / Logout
 *
 * @author John Suma
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { JSU_001_001.class })
public class UC001 {

}
