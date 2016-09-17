package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.SRE_012_001;

/**
 * Testsuite des UseCase Stempelung verifizieren
 *
 * @author Stefan Remund
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { SRE_012_001.class })
public class UC012 {

}
