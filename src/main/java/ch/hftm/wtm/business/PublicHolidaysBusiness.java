package ch.hftm.wtm.business;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.PublicHoliday;
import ch.hftm.wtm.model.Region;
import ch.hftm.wtm.persistence.PublicHolidaysPersistence;
import ch.hftm.wtm.persistence.RegionPersistence;

public class PublicHolidaysBusiness {
    private Client currentClient;
    private Person loggedInPerson;

    public PublicHolidaysBusiness() {

    }

    public void loadPersonalData(Person p) {
        if (p != null && p.getClient() != null) {
            loggedInPerson = p;
            currentClient = loggedInPerson.getClient();

            System.out.println("Client set to: " + currentClient.getCompanyName());
        } else
            System.out.println("Error on assigning current client.");
    }

    public List<Region> getAllRegions(Client c) {
        RegionPersistence rp = new RegionPersistence();
        return rp.getAllRegionsByClient(c);
    }

    public void persistPublicHoliday(PublicHoliday ph) {
        PublicHolidaysPersistence php = new PublicHolidaysPersistence();
        php.getEntityManager().getTransaction().begin();
        php.makePersistent(ph);
        php.getEntityManager().getTransaction().commit();
    }

    public PublicHoliday getPublicHoliday(Long id) {
        PublicHolidaysPersistence php = new PublicHolidaysPersistence();
        return php.getPublicHoliday(id);
    }

    public void updatePublicHoliday(PublicHoliday ph) {
        PublicHolidaysPersistence php = new PublicHolidaysPersistence();
        php.updatePublicHoliday(ph);
    }

    public void deleteRegion(Region region) {
        System.out.println("Deleting Region with id " + region.getId());
        RegionPersistence rp = new RegionPersistence();
        rp.deleteRegion(region);
    }

    public void persistRegion(Region newRegion) {
        if (newRegion.getClient() == null) {
            newRegion.setClient(currentClient);
            System.out.println("Region client has been set.");
        }

        try {
            RegionPersistence rp = new RegionPersistence();
            rp.getEntityManager().getTransaction().begin();
            rp.makePersistent(newRegion);
            rp.getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Region has not been saved due to an error.");
        }
    }

    public List<PublicHoliday> getAllPublicHolidays(Client c) {
        PublicHolidaysPersistence php = new PublicHolidaysPersistence();
        return php.getAllPublicHolidays(c);
    }

    public Set<PublicHoliday> getAllPublicHolidaysPerPerson(Person person) {
        Set<PublicHoliday> publicHolidays = new HashSet<PublicHoliday>();
        for (Region region : person.getEmploymentRegion()) {
            publicHolidays.addAll(region.getPublicHolidays());
        }
        return publicHolidays;
    }

    public double calculateTotalPublicHolidayHours(Person person, Date startDate, Date endDate) {
        double hours = 0;
        for (PublicHoliday publicHoliday : getAllPublicHolidaysPerPerson(person)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DATE, 1);
            cal.add(Calendar.DATE, publicHoliday.getDayOfTheYear());
            if (startDate.before(cal.getTime()) && endDate.after(cal.getTime()))
                hours += publicHoliday.getDuration();
        }
        return hours;
    }
}
