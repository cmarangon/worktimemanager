package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.MHA_002_001;

/**
 * Testsuite des UseCase Reports erstellen
 *
 * @author Marcel Haldimann
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { MHA_002_001.class })
public class UC002 {

}
