package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.SRE_013_001;
import ch.hftm.wtm.testing.tests.SRE_013_002;
import ch.hftm.wtm.testing.tests.SRE_013_003;

/**
 * Testsuite des UseCase Stammdaten verwalten
 *
 * @author Stefan Remund
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
        SRE_013_001.class,
        SRE_013_002.class,
        SRE_013_003.class
})
public class UC013 {

}
