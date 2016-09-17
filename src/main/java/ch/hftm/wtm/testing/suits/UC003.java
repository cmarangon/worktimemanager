package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.MHA_003_001;

/**
 * Testsuite des UseCase Eigene Reports erstellen
 *
 * @author Marcel Haldimann
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { MHA_003_001.class })
public class UC003 {

}
