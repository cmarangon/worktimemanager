package ch.hftm.wtm.persistence;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.FiscalPeriod;
import ch.hftm.wtm.model.MasterData;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.PublicHoliday;
import ch.hftm.wtm.model.Region;
import ch.hftm.wtm.model.Stamping;

/**
 *
 * @author HOEFI
 * @since 04.09.2016
 * @version 1.0
 */
public class DummyData {

    private static final int START_YEAR_OF_STAMPING = 2013;

    private ClientPersistence cp = new ClientPersistence();
    private MasterDataPersistence mdp = new MasterDataPersistence();
    private PersonPersistence pp = new PersonPersistence();
    private EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
    private StampingPersistence sp = new StampingPersistence();
    private AbsencePersistence ap = new AbsencePersistence();
    private AbsenceTypePersistence atp = new AbsenceTypePersistence();
    private FiscalPeriodPersistence fpp = new FiscalPeriodPersistence();
    private RegionPersistence rp = new RegionPersistence();

    public static void main(String[] args) {
        DummyData d = new DummyData();
        d.createDummyData();
    }

    public void createDummyData() {
        try {
            DatabaseConnection.beginTransaction();
            createClients();
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            System.out.println("Füre Haudi...DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!DONE!!!");
            DatabaseConnection.commitTransaction();
            // DatabaseConnection.rollbackTransaction();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            DatabaseConnection.rollbackTransaction();
        }
    }

    private int createRandom(int min, int max) {
        if (max < min) {
            int temp = max;
            max = min;
            min = temp;
        }
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private Date createRandomDate(int yearFrom, int monthFrom, int yearTo, int monthTo) {
        Calendar cal = Calendar.getInstance();
        int year = createRandom(yearFrom, yearTo);
        int month = createRandom(monthFrom, monthTo);
        int day = createRandom(1, 28);
        cal.set(year, month, day);
        return cal.getTime();
    }

    private Date createRandomTime(Date day, int hourFrom, int hourTo) {
        Calendar cal = Calendar.getInstance();
        int hour = createRandom(hourFrom, hourTo);
        int minute = createRandom(0, 59);
        cal.setTime(day);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    private long getMilliseconds(int hour, int minutes, int seconds) {
        return hour * 3600000 + minutes * 60000 + seconds * 1000;
    }

    private List<Date> getWorkingDays(Date startDate, Date endDate) {
        List<Date> days = new ArrayList<Date>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        if (endDate == null)
            endCal.set(Calendar.HOUR_OF_DAY, 0);
        else
            endCal.setTime(endDate);
        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                days.add(startCal.getTime());
            }
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
        return days;
    }

    private void createClients() {
        int clients = createRandom(3, 6);
        for (int i = 0; i < clients; i++) {
            System.out.println("Clients: " + (i + 1) + "/" + clients);

            Client cli = new Client();
            cli.setCompanyName("Client" + i);
            cp.makePersistent(cli);

            createRegions(cli);
            createPublicHolidays(cli);
            createMasterData(cli);
            createAbsenceTypes(cli);
            createFiscalPeriods(cli);

            Person p = createClientAdmin(cli);
            createPersons(cli, p);
        }
    }

    private void createRegions(Client cli) {
        Region r = new Region();
        r.setCountry("Schweiz");
        r.setName("Bern");
        r.setClient(cli);
        rp.makePersistent(r);

        r = new Region();
        r.setCountry("Schweiz");
        r.setName("Zürich");
        r.setClient(cli);
        rp.makePersistent(r);

        r = new Region();
        r.setCountry("Schweiz");
        r.setName("Zug");
        r.setClient(cli);
        rp.makePersistent(r);

        r = new Region();
        r.setCountry("Schweiz");
        r.setName("Graubünden");
        r.setClient(cli);
        rp.makePersistent(r);
        System.out.println("Regions created...");
    }

    private void createPublicHolidays(Client cli) {
        String[] texts = { "Hanukkah", "Jesus stirb mich tod", "Happy Kadaver",
                "Neujahr", "Ramadan", "Ostern", "Carfriday", "Fasnacht",
                "Sylvester", "Auffahrt", "Pfingsten", "1. August" };
        int holidays = createRandom(5, 12);
        for (int i = 0; i < holidays; i++) {
            PublicHoliday ph = new PublicHoliday();
            ph.setClient(cli);
            ph.setDayOfTheYear(createRandom(1, 365));
            ph.setDuration((double) createRandom(4, 8));
            ph.setTitle(texts[createRandom(0, 11)]);
            ph.setAppliedRegions(new HashSet<Region>());
            List<Region> r = cli.getClientRegions();
            int regions = createRandom(1, r.size() - 1);
            for (int j = 0; j < regions; j++) {
                ph.getAppliedRegions().add(r.get(createRandom(0, r.size() - 1)));
            }
        }
        System.out.println("PublicHolidays created...");
    }

    private void createMasterData(Client cli) {
        MasterData md = new MasterData();
        md.setClient(cli);
        md.setStartFiscalYear(1);
        md.setEndFiscalYear(365);
        md.setDefaultOvertimeMaximum(createRandom(500, 5000));
        md.setDefaultAnnualVacationDays(25);
        switch (createRandom(0, 2)) {
            case 0:
                md.setDefaultDailyWorkingHours(8.0);
                break;
            case 1:
                md.setDefaultDailyWorkingHours(8.25);
                break;
            case 2:
                md.setDefaultDailyWorkingHours(8.5);
                break;
        }
        mdp.makePersistent(md);
        System.out.println("MasterData created...");
    }

    private void createAbsenceTypes(Client cli) {
        AbsenceType at = new AbsenceType();
        at.setName("Ferien");
        at.setClient(cli);
        atp.makePersistent(at);

        at = new AbsenceType();
        at.setName("Krank");
        at.setClient(cli);
        atp.makePersistent(at);

        at = new AbsenceType();
        at.setName("Militär");
        at.setClient(cli);
        atp.makePersistent(at);

        at = new AbsenceType();
        at.setName("Mutterschaft");
        at.setClient(cli);
        atp.makePersistent(at);

        int p = createRandom(0, 100);
        if (p < 25) {
            at = new AbsenceType();
            at.setName("Umzug");
            at.setClient(cli);
            atp.makePersistent(at);
        }

        p = createRandom(0, 100);
        if (p < 25) {
            at = new AbsenceType();
            at.setName("Hochzeit");
            at.setClient(cli);
            atp.makePersistent(at);
        }

        p = createRandom(0, 100);
        if (p < 3) {
            at = new AbsenceType();
            at.setName("Jemand abstechen");
            at.setClient(cli);
            atp.makePersistent(at);
        }
        System.out.println("Absences created...");
    }

    private void createFiscalPeriods(Client cli) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        for (int y = START_YEAR_OF_STAMPING; y <= cal.get(Calendar.YEAR); y++) {
            FiscalPeriod fp = new FiscalPeriod();
            fp.setClient(cli);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            fp.setStartDate(c.getTime());

            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            fp.setEndDate(c.getTime());
            fpp.makePersistent(fp);
        }
        System.out.println("FiscalPeriods created...");
    }

    private Person createClientAdmin(Client cli) {
        Person p = new Person();
        p.setClient(cli);
        p.setFirstName(cli.getCompanyName());
        p.setLastName("Admin");
        p.setEmail(cli.getCompanyName() + ".Admin@dummy.ch");
        p.setPassword("123");
        p.setBirthDate(createRandomDate(1950, 0, 1986, 11));
        p.setAssignedUserRole(UserRole.ADMIN);
        cli.setClientAdmin(p);
        pp.makePersistent(p);

        createEmploymentPeriods(p);
        return p;
    }

    private void createPersons(Client cli, Person supervisor) {
        int persons = createRandom(6, 12);
        for (int i = 0; i < persons; i++) {
            System.out.println("Persons: " + (i + 1) + "/" + persons);
            Person p = new Person();
            p.setClient(cli);
            p.setFirstName(cli.getCompanyName());
            String lastName = String.format("%03d", i);
            p.setLastName(lastName);
            p.setEmail(cli.getCompanyName() + "." + lastName + "@dummy.ch");
            p.setPassword("123");
            p.setBirthDate(createRandomDate(1950, 0, 1996, 11));
            p.setAssignedUserRole(UserRole.USER);
            p.setSupervisor(supervisor);
            pp.makePersistent(p);

            createEmploymentPeriods(p);
        }
    }

    private void createEmploymentPeriods(Person p) {
        Calendar cal = Calendar.getInstance();
        Date endDate = null;
        int periods = createRandom(1, 3);
        for (int i = 0; i < periods; i++) {
            System.out.println("EmploymentPeriod: " + (i + 1) + "/" + periods);
            EmploymentPeriod ep = new EmploymentPeriod();
            ep.setEmployee(p);

            // Valid from/to
            if (i == 0) {
                cal.setTime(createRandomDate(START_YEAR_OF_STAMPING, 0, 2015, 12));
                ep.setValidFrom(cal.getTime());
            } else {
                cal.setTime(endDate);
                cal.add(Calendar.DATE, 1);
                ep.setValidFrom(cal.getTime());
            }
            if (i < periods - 1) {
                cal.add(Calendar.MONTH, 1);
                endDate = createRandomDate(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        2016,
                        8);
                ep.setValidTo(endDate);
                ep.setActive(false);
            }

            // Daily working hours
            Time t = null;
            switch (createRandom(0, 2)) {
                case 0:
                    t = new Time(getMilliseconds(8, 0, 0));
                    ep.setDailyWorkingHours(t);
                    break;
                case 1:
                    t = new Time(getMilliseconds(8, 15, 0));
                    ep.setDailyWorkingHours(t);
                    break;
                case 2:
                    t = new Time(getMilliseconds(8, 30, 0));
                    ep.setDailyWorkingHours(t);
                    break;
            }

            // Employment level
            switch (createRandom(0, 2)) {
                case 0:
                    ep.setEmploymentLevelPercent(80);
                    break;
                case 1:
                    ep.setEmploymentLevelPercent(90);
                    break;
                case 2:
                    ep.setEmploymentLevelPercent(100);
                    break;
            }
            epp.makePersistent(ep);

            createStampings(ep);
        }
    }

    private void createStampings(EmploymentPeriod ep) {
        List<Date> days = getWorkingDays(ep.getValidFrom(), ep.getValidTo());
        for (Date d : days) {
            int p = createRandom(0, 100);
            // 99% Chance
            if (p > 1) {

                // Morgen
                p = createRandom(0, 100);
                if (p > 1) {
                    Stamping s = new Stamping();
                    s.setAssignedPerson(ep);
                    s.setStampingTime(createRandomTime(d, 5, 9));
                    sp.makePersistent(s);
                    s = new Stamping();
                    s.setAssignedPerson(ep);
                    s.setStampingTime(createRandomTime(d, 11, 12));
                    sp.makePersistent(s);
                } else {
                    createAbsence(ep, d, true);
                }

                // Nachmittag
                p = createRandom(0, 100);
                if (p > 1) {
                    Stamping s = new Stamping();
                    s.setAssignedPerson(ep);
                    s.setStampingTime(createRandomTime(d, 12, 13));
                    sp.makePersistent(s);
                    s = new Stamping();
                    s.setAssignedPerson(ep);
                    s.setStampingTime(createRandomTime(d, 16, 19));
                    sp.makePersistent(s);
                } else {
                    createAbsence(ep, d, true);
                }
            } else {
                createAbsence(ep, d, false);
            }
        }
    }

    private void createAbsence(EmploymentPeriod ep, Date day, boolean isHalfDay) {
        Absence a = new Absence();
        a.setPerson(ep);
        a.setStart(day);
        a.setEnd(day);
        if (isHalfDay)
            a.setDurationHours(createRandomTime(day, 3, 5));
        else
            a.setDurationHours(ep.getDailyWorkingHours());
        List<AbsenceType> atl = atp.getAbsenceTypesFromClient(ep.getEmployee().getClient());
        int i = createRandom(0, atl.size() - 1);
        a.setAssignedAbsenceType(atl.get(i));
        ap.makePersistent(a);
    }

}
