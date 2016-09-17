package ch.hftm.wtm.testing.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.testing.tests.DST_011_001;

/**
 * Testsuite des UseCase Alle Stempelungen korrigieren
 *
 * @author Daniel Steiner
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { DST_011_001.class })
public class UC011 {

}
