package ch.hftm.wtm.business;

import java.util.Date;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.ClientPersistence;
import ch.hftm.wtm.persistence.DatabaseConnection;
import ch.hftm.wtm.persistence.PersonPersistence;

/**
 * @author HOEFI
 * @since 06.06.2016
 * @version 1.0
 */
public class PersonBusiness {

    public PersonBusiness() {}

    @Override
    protected void finalize() {
        System.out.println("Destroy PersonBusiness");
        DatabaseConnection.closeEntityManager();
    }

    public void createSystemAdministrator() {
        if (!personExists("abdi@gilette.ch")) {
            Person person = new Person();
            person.setFirstName("Abdi");
            person.setLastName("Gilette");
            person.setEmail("abdi@gilette.ch");
            person.setPassword("rasiert");
            person.setBirthDate(new Date());
            person.setAssignedUserRole(UserRole.SYSADMIN);
            savePerson(person);
            ClientPersistence cp = new ClientPersistence();
            Client client = new Client();
            client.setCompanyName("Abdi Ltd.");
            cp.save(client);
            person.setClient(client);
            savePerson(person);
        }
    }

    public void savePerson(Person person) {
        PersonPersistence personPersistence = new PersonPersistence();
        DatabaseConnection.beginTransaction();
        personPersistence.makePersistent(person);
        DatabaseConnection.commitTransaction();
    }

    // TODO: Das ist ein Platzhalter damit man arbeiten kann. Schön machen!
    public boolean personExists(String email) {
        PersonPersistence personPersistence = new PersonPersistence();
        if (personPersistence.findPersonByEmail(email) != null) {
            return true;
        }
        return false;
    }

    public Person getPersonByEmail(String email) {
        PersonPersistence pp = new PersonPersistence();
        return pp.findPersonByEmail(email);
    }
}
