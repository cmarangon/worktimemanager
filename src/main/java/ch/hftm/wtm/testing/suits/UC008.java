package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.DST_008_001;

/**
 * Testsuite des UseCase Ein- / Ausstempeln
 *
 * @author Daniel Steiner
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { DST_008_001.class })
public class UC008 {

}
