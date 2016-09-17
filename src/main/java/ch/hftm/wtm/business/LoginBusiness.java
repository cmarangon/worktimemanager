package ch.hftm.wtm.business;
    
import javax.persistence.NoResultException;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.exception.LoginPasswordNotCorrectException;
import ch.hftm.wtm.exception.LoginUserHasInactiveClient;
import ch.hftm.wtm.exception.LoginUserNotFoundException;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.PersonPersistence;

/**
 * Businesslogik für den Loginvorgang.
 *
 * @author srdjankovacevic
 * @since 07.07.2016
 * @version 1.0
 */
public class LoginBusiness {

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Login#veryLogin(java.lang.String, java.lang.String)
     */
    public Person verifyLogin(String userId, String password) throws LoginPasswordNotCorrectException,
            LoginUserNotFoundException, LoginUserHasInactiveClient {
        Person person;
        try {
            // Hole die Person aus der Datenbank
            PersonPersistence pp = new PersonPersistence();
            person = pp.findPersonByEmail(userId);
            if (person == null) {
                throw new LoginUserNotFoundException();
            } else {

                // Kontrolliere das Passwort
                if (!verifyPassword(person, password)) {
                    throw new LoginPasswordNotCorrectException();
                }

                // Kontrolliere ob Mandant aktiv ist (nicht für Sysadmin)
                if (!person.getClient().isActive() && !person.getAssignedUserRole().equals(UserRole.SYSADMIN)) {
                    throw new LoginUserHasInactiveClient();
                }

            }
        } catch (NoResultException nre) {
            throw new LoginUserNotFoundException();
        }
        return person;
    }

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Login#veryPassword(ch.hftm.wtm.model.Person, java.lang.String)
     */
    public boolean verifyPassword(Person person, String password) throws LoginPasswordNotCorrectException {
        return password.equals(person.getPassword());
    }
}