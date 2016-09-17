package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.CMA_014_001;
import ch.hftm.wtm.testing.tests.CMA_014_002;

/**
 * Testsuite des UseCase Mandant verwalten
 *
 * @author Claudio Marangon
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
        CMA_014_001.class,
        CMA_014_002.class
})
public class UC014 {

}
