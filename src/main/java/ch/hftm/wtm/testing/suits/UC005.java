package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.RHO_005_001;
import ch.hftm.wtm.testing.tests.RHO_005_002;
import ch.hftm.wtm.testing.tests.RHO_005_003;
import ch.hftm.wtm.testing.tests.RHO_005_004;

/**
 * Testsuite des UseCase Jahr abschliessen
 *
 * @author Reto Hofer
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
        RHO_005_001.class,
        RHO_005_002.class,
        RHO_005_003.class,
        RHO_005_004.class
})
public class UC005 {

}
