package ch.hftm.wtm.testing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.PublicHoliday;
import ch.hftm.wtm.model.Region;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.AbsencePersistence;
import ch.hftm.wtm.persistence.ClientPersistence;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;
import ch.hftm.wtm.persistence.GenericPersistence;
import ch.hftm.wtm.persistence.PersonPersistence;
import ch.hftm.wtm.persistence.PublicHolidaysPersistence;
import ch.hftm.wtm.persistence.RegionPersistence;
import ch.hftm.wtm.persistence.StampingPersistence;



public class CreatingStampingsStu extends GenericPersistence<Stamping, Long> {


    static Person person2 = new Person();
    static Person person3 = new Person();
    static Person person4 = new Person();
    
    public static void main(String[] args) {
        
        PersonPersistence pp = new PersonPersistence();
        
        EmploymentPeriod ep = new EmploymentPeriod();        
        
        Client c = createClient();
        createRegions(c);
        
        // Vorgesezter
        Person person = createPerson();
        persistPerson(person, c);
        

        ep.setEmployee(person);
        ep.setEmploymentLevelPercent(80);
        ep.setActive(false);
        ep.setYearlyVacationDays(10D);
        ep.setMaxOvertime(new Date(2014,01,01, 11, 10));
        ep.setValidFrom(new Date(2013,01,01));
        ep.setValidTo(new Date(2013,12,31));
        
        EmploymentPeriod ep2 = new EmploymentPeriod();
        ep2.setEmployee(person);
        ep2.setEmploymentLevelPercent(50);
        ep2.setYearlyVacationDays(20.5);
        ep2.setMaxOvertime(new Date(2014,01,01, 23, 10));
        ep2.setValidFrom(new Date(2014,01,01));
        ep2.setValidTo(new Date(2014,12,31));
        ep2.setActive(false);
        
        EmploymentPeriod ep3 = new EmploymentPeriod();
        ep3.setEmployee(person);
        ep3.setEmploymentLevelPercent(20);
        ep3.setYearlyVacationDays(15D);
        ep3.setMaxOvertime(new Date(2014,01,01, 01, 10));
        ep3.setValidFrom(new Date(2015,01,01,10,11));
        ep3.setActive(true);
        

        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        epp.getEntityManager().getTransaction().begin();
        epp.makePersistent(ep);
        epp.makePersistent(ep2);
        epp.makePersistent(ep3);
        epp.getEntityManager().getTransaction().commit();
        
        persistStamping(fillStampings(person));        
        
        Person subPerson = createEmployees(person, c);
        
        fillAbsences(subPerson);
        persistStamping(fillStampings(subPerson));
        
        System.out.println("Created!!");
    }

    private static void createRegions(Client c)
    {
        Region r1, r2, r3;
        
        r1 = new Region();
        r1.setName("Bern");
        r1.setCountry("Switzerlande");
        r1.setClient(c);
        

        r2 = new Region();
        r2.setName("Solothurn");
        r2.setCountry("Switzerlande");
        r2.setClient(c);
        
        r3 = new Region();
        r3.setName("Zürich");
        r3.setCountry("Switzerlande");
        r3.setClient(c);
        
        RegionPersistence rp = new RegionPersistence();
        rp.getEntityManager().getTransaction().begin();
        rp.makePersistent(r1);
        rp.makePersistent(r2);
        rp.makePersistent(r3);
        rp.getEntityManager().getTransaction().commit();
        
        rp.getEntityManager().getTransaction().begin();
        createPublicHolidays(c, r1, r2, r3);
        rp.getEntityManager().getTransaction().commit();
    }
    
    private static void createPublicHolidays(Client c, Region r1,Region r2,Region r3)
    {
        PublicHolidaysPersistence php = new PublicHolidaysPersistence(); 
                        
        PublicHoliday ph1 = new PublicHoliday();
        ph1.setClient(c);
        ph1.setDayOfTheYear(100);
        ph1.setDuration(8.5);
        ph1.setTitle("Weihnachten");
        ph1.setIsActive(true);
        Set<Region> regions = new HashSet<Region>();
        regions.add(r1);
        regions.add(r2);
        ph1.setAppliedRegions(regions);
        php.makePersistent(ph1);
        
        ph1 = new PublicHoliday();
        ph1.setClient(c);
        ph1.setDayOfTheYear(100);
        ph1.setDuration(8.5);
        ph1.setTitle("Weihnachten");
        ph1.setIsActive(true);
        regions = new HashSet<Region>();
        regions.add(r1);
        regions.add(r3);
        ph1.setAppliedRegions(regions);
        
        php.makePersistent(ph1);
    }
    
    private static Client createClient()
    {
        Client c = new Client();
        c.setCompanyName("asd");
        
        ClientPersistence cp = new ClientPersistence();
        cp.getEntityManager().getTransaction().begin();
        cp.makePersistent(c);
        cp.getEntityManager().getTransaction().commit();
        
        return c;
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

    public static Person createEmployees(Person supervisor, Client c) {
        person2.setFirstName("kove");
        person2.setLastName("test");
        person2.setEmail("kove@hftm.ch");
        person2.setPassword("kove");
        person2.setAssignedUserRole(UserRole.USER);
        person2.setBirthDate(new Date());
        person2.setSupervisor(supervisor);
        persistPerson(person2, c);
        
        EmploymentPeriod ep2 = new EmploymentPeriod();
        ep2.setEmployee(person2);
        ep2.setEmploymentLevelPercent(80);
        ep2.setActive(true);
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        
        epp.getEntityManager().getTransaction().begin();
        epp.makePersistent(ep2);
        epp.getEntityManager().getTransaction().commit();
        
        
        person3.setFirstName("stu");
        person3.setLastName("test");
        person3.setEmail("stu@hftm.ch");
        person3.setPassword("stu");
        person3.setAssignedUserRole(UserRole.USER);
        person3.setBirthDate(new Date());
        person3.setSupervisor(supervisor);
        persistPerson(person3,c );
        
        EmploymentPeriod ep3 = new EmploymentPeriod();
        ep3.setEmployee(person3);
        ep3.setEmploymentLevelPercent(60);
        ep3.setActive(true);
        
        epp.getEntityManager().getTransaction().begin();
        epp.makePersistent(ep3);
        epp.getEntityManager().getTransaction().commit();
        
        person4.setFirstName("haudi");
        person4.setLastName("test");
        person4.setEmail("haudi@hftm.ch");
        person4.setPassword("haudi");
        person4.setAssignedUserRole(UserRole.USER);
        person4.setBirthDate(new Date());
        person4.setSupervisor(supervisor);
        persistPerson(person4,c );
        
        EmploymentPeriod ep4 = new EmploymentPeriod();
        ep4.setEmployee(person4);
        ep4 .setEmploymentLevelPercent(40);
        ep4.setActive(true);
        
        epp.getEntityManager().getTransaction().begin();
        epp.makePersistent(ep4);
        epp.getEntityManager().getTransaction().commit();
        
        return person2;
    }

    public static void persistPerson(Person person, Client c) {
        PersonPersistence pp = new PersonPersistence();
        person.setClient(c);
        
        pp.getEntityManager().getTransaction().begin();
        person = pp.makePersistent(person);
        pp.getEntityManager().getTransaction().commit();
    }

    public static void persistStamping(List<Stamping> ar) {
        StampingPersistence sp = new StampingPersistence();
        
        sp.getEntityManager().getTransaction().begin();
        for (Stamping st : ar) {
            sp.makePersistent(st);
        }
        sp.getEntityManager().getTransaction().commit();
    }

    @SuppressWarnings("deprecation")
    public static void fillAbsences(Person person) {
        ArrayList<Absence> ar = new ArrayList<>();
        AbsencePersistence ap = new AbsencePersistence();
        Calendar c = Calendar.getInstance();
        
        for (int a = 0; a < 10; a++) {
            Absence absence = new Absence();
            
            c.add(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 7);            
            absence.setStart(c.getTime());
            
            c.set(Calendar.HOUR_OF_DAY, 17);
            absence.setEnd(c.getTime());
            
            absence.setIsVerified(null);
            absence.setPerson(person.getEmploymentPeriods().get(0));
            
            ap.getEntityManager().getTransaction().begin();
            ap.makePersistent(absence);
            ap.getEntityManager().getTransaction().commit();
        }
    }
    
    public static ArrayList<Stamping> fillStampings(Person person) {
        ArrayList<Stamping> ar = new ArrayList<>();
        Calendar c = Calendar.getInstance();

        for (int a = 0; a < 20; a++) {
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
            st4.setVerificationStatus("Nicht Verifiziert");
            st4.setAssignedPerson(person.getEmploymentPeriods().get(0));
            ar.add(st4);

        }
        return ar;
    }
}
