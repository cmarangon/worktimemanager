package ch.hftm.wtm.business;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;

public class ManageEmploymentPeriodsBusiness {

    protected Logger logger = Logger.getLogger(getClass());

    /**
     * Submit the Changes made on an EmploymentPeriod
     * 
     * @param ep
     */
    public void submitChanges(EmploymentPeriod ep) {
        System.out.println("Submitchanges ep Id: " + ep.getId());
        EmploymentPeriodPersistence mepp = new EmploymentPeriodPersistence();
        EmploymentPeriod dbEmploymentPeriod = mepp.getEmploymentPeriod(ep);

        System.out.println(dbEmploymentPeriod.getEmploymentLevelPercent() + " new: " + ep.getEmploymentLevelPercent());

        if (dbEmploymentPeriod.getEmploymentLevelPercent() != ep.getEmploymentLevelPercent() ||
                dbEmploymentPeriod.getMaxOvertime() != ep.getMaxOvertime() ||
                dbEmploymentPeriod.getDailyWorkingHours() != ep.getDailyWorkingHours()) {
            logger.info("Employment Percent changed, creating new EmploymentPeriod");
            generateNewEmploymentPeriod(ep, dbEmploymentPeriod);
            deactivateOldEmploymentPeriod(dbEmploymentPeriod);
        } else
            System.out.println("else");
    }

    public void detachEmploymentPeriod(EmploymentPeriod ep) {
        EmploymentPeriodPersistence mepp = new EmploymentPeriodPersistence();
        System.out.println("Detach EmploymentPeriod.");
        mepp.detachEntity(ep);
    }

    public Set<EmploymentPeriod> getAllEmploymentPeriods(Person p) {
        EmploymentPeriodPersistence mepp = new EmploymentPeriodPersistence();
        HashSet<EmploymentPeriod> allEmploymentPeriodSet = new HashSet<>();

        allEmploymentPeriodSet.addAll(mepp.getEmploymentPeriodsOrdered(p));
        return allEmploymentPeriodSet;
    }

    /**
     * deactivates an EmploymentPeriod object
     * 
     * @param ep EmploymentPeriod object to deactivate.
     */
    private void deactivateOldEmploymentPeriod(EmploymentPeriod ep) {
        EmploymentPeriodPersistence mepp = new EmploymentPeriodPersistence();
        mepp.deactivateEmploymentPeriod(ep);
    }

    /**
     * Generates a new EmploymentPeriod based on the old and the new EmploymentPeriods
     * 
     * @param newEP an Object with the new Values.
     * @param oldEP an Object with the old Values.
     */
    private void generateNewEmploymentPeriod(EmploymentPeriod newEP, EmploymentPeriod oldEP) {
        EmploymentPeriod epToPersist = new EmploymentPeriod();
        epToPersist.setActive(true);
        epToPersist.setDailyWorkingHours(newEP.getDailyWorkingHours());
        epToPersist.setEmploymentLevelPercent(newEP.getEmploymentLevelPercent());
        epToPersist.setMaxOvertime(newEP.getMaxOvertime());
        epToPersist.setYearlyVacationDays(newEP.getYearlyVacationDays());

        epToPersist.setEmployee(oldEP.getEmployee());
        epToPersist.setValidFrom(Calendar.getInstance().getTime());

        EmploymentPeriodPersistence mepp = new EmploymentPeriodPersistence();
        mepp.persistEmploymentPeriod(epToPersist);
    }

    public EmploymentPeriod getActiveEmploymentPeriod(Person p) {
        EmploymentPeriodPersistence pp = new EmploymentPeriodPersistence();

        return pp.getActiveEmploymentPeriod(p.getId());
    }
}
