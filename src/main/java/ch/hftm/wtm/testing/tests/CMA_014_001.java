package ch.hftm.wtm.testing.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.hftm.wtm.business.ClientBusiness;
import ch.hftm.wtm.business.LoginBusiness;
import ch.hftm.wtm.business.PersonBusiness;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;

/**
 * UseCase CMA_014_001
 *
 * Mandanten verwalten
 * Folgende Fälle werden getestet:
 * <ul>
 * <li>Einen neuen Mandanten anlegen</li>
 * <li>Zwei Mandanten mit gleichem Namen anlegen</li>
 * <li>Login eines Benutzers an einem inaktiven Mandanten</li>
 * </ul>
 *
 * @author Claudio Marangon
 */
public class CMA_014_001 {
    private static ClientBusiness cb = null;
    private static PersonBusiness pb = null;
    private static LoginBusiness lb = null;

    @BeforeClass
    public static void setUp() {
        // runs before all tests

        cb = new ClientBusiness();
        lb = new LoginBusiness();
    }

    @Before
    public void beforeEachTest() {
        // runs before each test
    }

    /**
     * Check if we can insert a new client
     */
    @Test
    public void insertNewClient() {
        Client newClient = new Client();
        newClient.setCompanyName("FirstCompany");

        assertTrue(cb.saveClient(newClient));
    }

    /**
     * Check if we can insert a new client with a duplicate name
     */
    @Test
    public void insertSameClient() {
        Client client1 = new Client();
        client1.setCompanyName("SecondCompany");
        cb.saveClient(client1);

        Client client2 = new Client();
        client2.setCompanyName("SecondCompany");

        assertFalse(cb.saveClient(client2));
    }

    /**
     * Check if we can login on a active client
     */
    @Test
    public void loginOnActiveClient() {
        Person person = null;
        try {
            person = lb.verifyLogin("heinz.muster@hftm.ch", "asdf");
        } catch (Exception e) {}

        assertTrue(person != null);
    }

    /**
     * Check if we can login on a inactive client
     */
    @Test
    public void loginOnInactiveClient() {
        Person person = null;
        try {
            person = lb.verifyLogin("kguenter@microsoft.com", "asdf");
        } catch (Exception e) {}

        assertTrue(person == null);
    }

    @After
    public void afterEachTest() {
        // runs after each test
    }

    @AfterClass
    public static void tearDown() {
        // runs after all tests
    }

}
