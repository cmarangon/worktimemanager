package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.DST_009_001;
import ch.hftm.wtm.testing.tests.DST_009_002;

/**
 * Testsuite des UseCase Stempelung korrigieren
 *
 * @author Daniel Steiner
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
        DST_009_001.class,
        DST_009_002.class
})
public class UC009 {

}
