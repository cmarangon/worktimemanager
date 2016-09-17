package ch.hftm.wtm.testing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;
import ch.hftm.wtm.persistence.GenericPersistence;
import ch.hftm.wtm.persistence.PersonPersistence;
import ch.hftm.wtm.persistence.StampingPersistence;

public class CreatingStampings extends GenericPersistence<Stamping, Long> {

    static Person person2 = new Person();
    static Person person3 = new Person();
    static Person person4 = new Person();

    public static void main(String[] args) {
        
        // Vorgesezter
        Person person = createPerson();
        //person.setEmployees(fillEmployees());
        persistPerson(person);
        createEmployees(person);
        EmploymentPeriod ep = new EmploymentPeriod();
        ep.setEmployee(person);
        ep.setActive(true);
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        epp.makePersistent(ep);
        

        persistStamping(fillStampings(person));

        System.out.println("Created!!");
    }
    
    public static ArrayList<Person> fillEmployees(){
        ArrayList<Person> ar = new ArrayList<>();
        ar.add(person2);
        ar.add(person3);
        ar.add(person4);
        return ar;
    }

    public static Person createPerson() {
        Person person = new Person();
        person.setFirstName("test");
        person.setLastName("test");
        person.setEmail("test@hftm.ch");
        person.setPassword("test");
        person.setAssignedUserRole(UserRole.SYSADMIN);
        person.setBirthDate(new Date());
        return person;
    }

    public static void createEmployees(Person supervisor) {
        person2.setFirstName("kove");
        person2.setLastName("test");
        person2.setEmail("kove@hftm.ch");
        person2.setPassword("kove");
        person2.setAssignedUserRole(UserRole.USER);
        person2.setBirthDate(new Date());
        person2.setSupervisor(supervisor);
        persistPerson(person2);
        EmploymentPeriod ep = new EmploymentPeriod();
        ep.setEmployee(person2);
        ep.setActive(true);
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        epp.makePersistent(ep);
        
        
        person3.setFirstName("stu");
        person3.setLastName("test");
        person3.setEmail("stu@hftm.ch");
        person3.setPassword("stu");
        person3.setAssignedUserRole(UserRole.USER);
        person3.setBirthDate(new Date());
        person3.setSupervisor(supervisor);
        persistPerson(person3);
        EmploymentPeriod ep3 = new EmploymentPeriod();
        ep.setEmployee(person3);
        ep.setActive(true);
        epp.makePersistent(ep3);
        
        person4.setFirstName("haudi");
        person4.setLastName("test");
        person4.setEmail("haudi@hftm.ch");
        person4.setPassword("haudi");
        person4.setAssignedUserRole(UserRole.USER);
        person4.setBirthDate(new Date());
        person4.setSupervisor(supervisor);
        persistPerson(person4);
        EmploymentPeriod ep4 = new EmploymentPeriod();
        ep.setEmployee(person4);
        ep.setActive(true);
        epp.makePersistent(ep4);
    }

    public static void persistPerson(Person person) {
        PersonPersistence pp = new PersonPersistence();
        pp.makePersistent(person);
    }

    public static void persistStamping(List<Stamping> ar) {
        StampingPersistence sp = new StampingPersistence();

        for (Stamping st : ar) {
            sp.makePersistent(st);
        }
    }

    public static ArrayList<Stamping> fillStampings(Person person) {
        ArrayList<Stamping> ar = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        for (int a = 0; a < 200; a++) {
            // Stempelung am morgen kommen
            Stamping st = new Stamping();
            c.add(Calendar.DATE, 1);

            c.set(Calendar.HOUR_OF_DAY, 7);
            st.setStampingTime(c.getTime());
            st.setVerificationStatus("Verifiziert");
            st.setAssignedPerson(person.getEmploymentPeriods().get(0));
            ar.add(st);
            // Stempelung am morgen gehen
            Stamping st2 = new Stamping();
            c.set(Calendar.HOUR_OF_DAY, 12);
            st2.setStampingTime(c.getTime());
            st2.setVerificationStatus("Verifiziert");
            st2.setAssignedPerson(person.getEmploymentPeriods().get(0));
            ar.add(st2);

            // Nachmittag kommen
            Stamping st3 = new Stamping();
            c.set(Calendar.HOUR_OF_DAY, 13);
            st3.setStampingTime(c.getTime());
            st3.setVerificationStatus("Verifiziert");
            st3.setAssignedPerson(person.getEmploymentPeriods().get(0));
            ar.add(st3);

            // NAchmittag gehen
            Stamping st4 = new Stamping();
            c.set(Calendar.HOUR_OF_DAY, 17);
            st4.setStampingTime(c.getTime());
            st4.setVerificationStatus("Verifiziert");
            st4.setAssignedPerson(person.getEmploymentPeriods().get(0));
            ar.add(st4);

        }
        return ar;
    }
}