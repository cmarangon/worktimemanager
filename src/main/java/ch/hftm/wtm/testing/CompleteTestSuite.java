package ch.hftm.wtm.testing;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.ClientPersistence;
import ch.hftm.wtm.persistence.DatabaseConnection;
import ch.hftm.wtm.persistence.PersonPersistence;
import ch.hftm.wtm.testing.suits.UC001;
import ch.hftm.wtm.testing.suits.UC002;
import ch.hftm.wtm.testing.suits.UC003;
import ch.hftm.wtm.testing.suits.UC004;
import ch.hftm.wtm.testing.suits.UC005;
import ch.hftm.wtm.testing.suits.UC006;
import ch.hftm.wtm.testing.suits.UC007;
import ch.hftm.wtm.testing.suits.UC008;
import ch.hftm.wtm.testing.suits.UC009;
import ch.hftm.wtm.testing.suits.UC010;
import ch.hftm.wtm.testing.suits.UC011;
import ch.hftm.wtm.testing.suits.UC012;
import ch.hftm.wtm.testing.suits.UC013;
import ch.hftm.wtm.testing.suits.UC014;

@RunWith(Suite.class)
@SuiteClasses({ UC001.class,
        UC002.class,
        UC003.class,
        UC004.class,
        UC005.class,
        UC006.class,
        UC007.class,
        UC008.class,
        UC009.class,
        UC010.class,
        UC011.class,
        UC012.class,
        UC013.class,
        UC014.class })
public class CompleteTestSuite {

    @BeforeClass
    public static void setUpEnvironment() {
        System.out.println("Testdaten einlesen");
        /**
         * HIER SOLLEN (VORERST ALS KOMMENTARE) DIE TESTDATEN DEFINIERT WERDEN, WELCHE ALS VOR-
         * BEDINGUNGEN FÜR DIE TESTS NOTWENDIG SIND.
         * DIE TESTDATEN SOLLEN JE USECASE DEFINIERT WERDEN, ES SEI DENN, ES KÖNNEN TESTDATEN AUS
         * ANDEREN USECASES WIEDERVERWENDET WERDEN. FALLS DIES DER FALL IST, SOLL DIES SO VERMERKT
         * WERDEN.
         */
        DatabaseConnection.setTestDatabase(true);
        PersonPersistence pp = new PersonPersistence();
        ClientPersistence cp = new ClientPersistence();

        /*
         * UC-001 Login / Logout
         * Um die Funktion testen zu können, benötigen wir eine email (vorname.nachname@hftm.ch)
         * und ein Passwort
         */
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Suma");
        person.setEmail("john.suma@hftm.ch");
        person.setPassword("12345678");
        person.setBirthDate(new Date());
        // Person speichern
        pp.makePersistent(person);

        Person person2 = new Person();
        person2.setFirstName("s");
        person2.setLastName("r");
        person2.setEmail("s.r@hftm.ch");
        person2.setPassword("123");
        person2.setBirthDate(new Date());
        // Person speichern
        pp.makePersistent(person2);

        /* UC-002 Reports erstellen */

        /* UC-003 Eigene Reports erstellen */
        // Mitarbeiter mit einem vollen gestemeplten Monat
        // Mitarbeiter mit einem halb gestemeplten Monat (Kann der gleieche sein mit unterschiedlichem Monat)

        /* UC-004 Reports Mitarbeiter erstellen */
        // Vorgesetzter der beiden Mitarbeiter

        /* UC-005 Jahr abschliessen */
        // Mehrere Mitarbeiter mit einer geraden Anzahl an Stempelungen
        // Mehrere Mitarbeiter mit einer ungeraden Anzahl an Stempelungen
        // Mind. ein Fiskaljahr welches noch nicht abgeschlossen wurde
        // Mind. ein Fiskaljahr welches bereits abgeschlossen wurde
        // Mind. ein Fiskaljahr welches gerade noch aktiv ist.

        /* UC-006 Abwesenheit erfassen */

        /* UC-007 Abwesenheit verifizieren */

        /*
         * UC-008 Ein- / Ausstempeln
         * Benötigt keine Daten, nur einen User Mitarbeiter der die Stempelung durchführen kann.
         * In der Datenbank sollten die neuen Stempelungen sichtbar werden.
         */

        /*
         * UC-009 Stempelung korrigieren
         * Benötigt einige Stempelungen.
         */

        /*
         * UC-010 Stempelung unterstellter MA korrigieren
         * Benötigt einen User mit der Rolle des Vorgesetzten.
         * Dieser Vorgesetzter benötigt einen Mitarbeiter.
         * Der Mitarbeiter benötigt einige Stempelungen.
         */

        /*
         * UC-011 Alle Stempelungen korrigieren
         * Benötigt einen User mit der Rolle das Admins
         * Mindestens zwei verschiedene Mitarbeiter.
         * Die Mitarbeiter benötigen einige Stempelungen.
         */

        /* UC-012 Stempelung verifizieren */

        /* UC-013 Stammdaten verwalten */

        /* UC-014 Mandant verwalten */
        // - Mandant mit Namen "HFTM"
        Client hftm = new Client("HFTM");
        cp.makePersistent(hftm);

        // - Kompletter Admin für HFTM
        Person hftm_admin = new Person();
        hftm_admin.setFirstName("Heinz");
        hftm_admin.setLastName("Muster");
        hftm_admin.setEmail("heinz.muster@hftm.ch");
        hftm_admin.setPassword("asdf");
        hftm_admin.setBirthDate(new Date());
        hftm_admin.setAssignedUserRole(UserRole.ADMIN);
        hftm_admin.setClient(hftm);
        pp.makePersistent(hftm_admin);

        // - Mandant mit Namen "Microsoft"
        Client microsoft = new Client("Microsoft");
        cp.makePersistent(microsoft);

        // - Kompletter Admin für Microsoft
        Person microsoft_admin = new Person();
        microsoft_admin.setFirstName("Karl");
        microsoft_admin.setLastName("Günter");
        microsoft_admin.setEmail("kguenter@microsoft.com");
        microsoft_admin.setPassword("asdf");
        microsoft_admin.setBirthDate(new Date());
        microsoft_admin.setAssignedUserRole(UserRole.ADMIN);
        microsoft_admin.setClient(microsoft);
        pp.makePersistent(microsoft_admin);

        // - Mandant deaktivieren
        microsoft.setActive(false);
        cp.makePersistent(microsoft);
    }

    @AfterClass
    public static void tearDownEnvironment() {
        System.out.println("Testdaten entfernen");
    }

}