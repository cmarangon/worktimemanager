package ch.hftm.wtm.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.AnnualBalanceSheet;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.FiscalPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.AnnualBalanceSheetPersistence;
import ch.hftm.wtm.persistence.DatabaseConnection;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;
import ch.hftm.wtm.persistence.FiscalPeriodPersistence;
import ch.hftm.wtm.persistence.ManageAbsencePersistence;
import ch.hftm.wtm.persistence.StampingPersistence;

/**
 * Businessmethoden für den Abschluss einer Periode.
 * 
 * @author HOEFI
 * @since 08.08.2016
 * @version 1.0
 */
public class FinalStatementBusiness {

    private Set<Person> employeeStampingOk = new HashSet<Person>();
    private Set<Person> employeeStampingNotOk = new HashSet<Person>();

    /**
     * Überprüft für alle MA eines Clients die Stempelungen innerhalb
     * einer bestimmten Periode. Die MA können anschliessen mittels
     * getPersonsWithCorrectStampings() und getPersonsWithInvalidStampings() geholt werden.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param client
     * @param fiscalPeriod
     */
    public void checkStampings(Client client, FiscalPeriod fiscalPeriod) {
        if (client == null) return;
        for (Person person : client.getEmployees()) {
            List<Stamping> timestamps = new ArrayList<Stamping>();
            for (EmploymentPeriod empPeriod : person.getEmploymentPeriods()) {
                if (empPeriod.isActive() || empPeriod.getValidTo().after(fiscalPeriod.getStartDate())) {

                    for (Stamping stamping : empPeriod.getTimestamps()) {
                        if (stamping.getStampingTime().after(fiscalPeriod.getStartDate())
                            && stamping.getStampingTime().before(fiscalPeriod.getEndDate())) timestamps.add(stamping);
                    }
                }
            }

            if (timestamps.size() > 0 && (timestamps.size() % 2) == 0)
                employeeStampingOk.add(person);
            else if (timestamps.size() > 0) employeeStampingNotOk.add(person);
        }
    }

    /**
     * MA welche durch die Methode checkStampings() abgefüllt worden sind und die Stempelungen i.O. sind.
     * Correct Stampings = alle Stempelungen innerhalb der Periode Modulo 2 = 0.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @return
     */
    public Set<Person> getPersonsWithCorrectStampings() {
        return employeeStampingOk;
    }

    /**
     * MA welche durch die Methode checkStampings() abgefüllt worden sind und die Stempelungen inkonsistent sind.
     * Correct Stampings = alle Stempelungen innerhalb der Periode Modulo 2 != 0.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @return
     */
    public Set<Person> getPersonsWithInvalidStampings() {
        return employeeStampingNotOk;
    }

    /**
     * Prüft ob ein Datum innerhalb eines bestimmten Datumsbereichs liegt.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param dateToCheck
     * @param startDate
     * @param endDate
     * @return Spezialfall: Wenn dateToCheck == NULL gibt die Methode FALSE zurück.
     */
    private boolean isDateBetweenDates(Date dateToCheck, Date startDate, Date endDate) {
        if (dateToCheck != null && startDate.before(dateToCheck) && endDate.after(dateToCheck)) {
            return true;
        }
        return false;
    }

    /**
     * Datumsdifferenz in einer bestimmten Time-Unit.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param date1
     * @param date2
     * @param timeUnit
     * @return
     */
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Berechnet die Arbeitsstunden (IST-Stunden) einer EmploymentPeriod innerhalb einer Fiskal Periode.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param empPeriod
     * @param fiscalPeriod
     * @return
     */
    private double calculateWorkedHours(EmploymentPeriod empPeriod, FiscalPeriod fiscalPeriod) {
        double workedMinutes = 0;
        Date startWorking = null;
        StampingPersistence stampingPersist = new StampingPersistence();
        for (Stamping stamping : stampingPersist.getStampingsPerPersonOrdered(empPeriod, fiscalPeriod.getStartDate(),
                                                                              fiscalPeriod.getEndDate())) {
            if (startWorking == null) {
                startWorking = stamping.getStampingTime();
            } else {
                workedMinutes += getDateDiff(startWorking, stamping.getStampingTime(), TimeUnit.MINUTES);
                startWorking = null;
            }
        }
        double workedHours = Math.floor(workedMinutes / 60);
        workedHours += (workedMinutes % 60) / 60;
        return workedHours;
    }

    /**
     * Berechnet die Arbeitsstunden (IST-Stunden) einer Person innerhalb einer Fiskal Periode.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param person
     * @param fiscalPeriod
     * @return
     */
    public double calculateWorkedHoursPerPerson(Person person, FiscalPeriod fiscalPeriod) {
        double workedHours = 0;
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        for (EmploymentPeriod empPeriod : epp.getEmploymentPeriodsOrdered(person)) {
            workedHours += calculateWorkedHours(empPeriod, fiscalPeriod);
        }
        return workedHours;
    }

    /**
     * Berechnet die SOLL-Arbeitsstunden einer Person innerhalb einer Fiskal Periode.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param person
     * @param fiscalPeriod
     * @return
     */
    public double calculateWorkingHourPerPerson(Person person, FiscalPeriod fiscalPeriod) {
        Date startDate = fiscalPeriod.getStartDate();
        Date endDate = fiscalPeriod.getEndDate();
        double workingHours = 0;
        double publicHolidayHours = 0;
        
        PublicHolidaysBusiness phb = new PublicHolidaysBusiness();
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        for (EmploymentPeriod empPeriod : epp.getEmploymentPeriodsOrdered(person)) {
            int workingDays = 0;
            if (isDateBetweenDates(empPeriod.getValidFrom(), startDate, endDate)) {
                startDate = empPeriod.getValidFrom();
                if (empPeriod.getValidTo() != null && empPeriod.getValidTo().before(endDate)){
                    publicHolidayHours = phb.calculateTotalPublicHolidayHours(person, startDate, empPeriod.getValidTo());
                    workingDays = getWorkingDaysBetweenTwoDates(startDate, empPeriod.getValidTo());
                    startDate = empPeriod.getValidTo();
                } else {
                    publicHolidayHours = phb.calculateTotalPublicHolidayHours(person, startDate, endDate);
                    workingDays = getWorkingDaysBetweenTwoDates(startDate, endDate);
                }
            } else if (isDateBetweenDates(empPeriod.getValidTo(), startDate, endDate)) {
                publicHolidayHours = phb.calculateTotalPublicHolidayHours(person, startDate, empPeriod.getValidTo());
                workingDays = getWorkingDaysBetweenTwoDates(startDate, empPeriod.getValidTo());
                startDate = empPeriod.getValidTo();
            } else if (empPeriod.isActive() && endDate.after(empPeriod.getValidFrom())
                            || empPeriod.getValidFrom().before(startDate) && empPeriod.getValidTo().after(endDate)) {
                publicHolidayHours = phb.calculateTotalPublicHolidayHours(person, startDate, endDate);
                workingDays = getWorkingDaysBetweenTwoDates(startDate, endDate);
            }
            workingHours += workingDays * getHoursInDecimal(empPeriod.getDailyWorkingHours())
                            * empPeriod.getEmploymentLevelPercent() / 100;
            publicHolidayHours = publicHolidayHours * empPeriod.getEmploymentLevelPercent() / 100;
        }
        workingHours -= calculateAbsenceTotal(person, fiscalPeriod);
        workingHours -= publicHolidayHours;
        return workingHours;
    }

    /**
     * Schliesst die Fiskal Periode eines Clients.
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param client
     * @param fiscalPeriod
     */
    public void closePeriod(Client client, FiscalPeriod fiscalPeriod) {
        double totalWorkedHours = 0;
        double totalWorkingHours = 0;
        for (Person person : client.getEmployees()) {
            totalWorkingHours += calculateWorkingHourPerPerson(person, fiscalPeriod);
            totalWorkedHours += calculateWorkedHoursPerPerson(person, fiscalPeriod);
        }
        AnnualBalanceSheet abs = new AnnualBalanceSheet();
        abs.setTitle("Abschluss");
        abs.setFiscalPeriod(fiscalPeriod);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) (totalWorkedHours - totalWorkingHours) * 3600000);
        abs.setTotalOvertime(cal.getTime());
        abs.setTotalUntakenVacationDays(0);

        try {
            DatabaseConnection.beginTransaction();
            AnnualBalanceSheetPersistence absp = new AnnualBalanceSheetPersistence();
            absp.makePersistent(abs);

            FiscalPeriodPersistence fpp = new FiscalPeriodPersistence();
            FiscalPeriod fp = fpp.findById(fiscalPeriod.getId());
            Calendar cal2 = Calendar.getInstance();
            fp.setCompletedOn(cal2.getTime());
            fp.setCompletedBy(client.getClientAdmin());
            fp.setAnnualBalanceSheet(abs);
            fpp.makePersistent(fp);
            fpp.flush();
            DatabaseConnection.commitTransaction();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            DatabaseConnection.rollbackTransaction();
        }
    }

    /**
     * 
     * @author HOEFI
     * @since 12.09.2016
     * @param person
     * @param fiscalPeriod
     * @return
     */
    private double calculateAbsenceTotal(Person person, FiscalPeriod fiscalPeriod) {
        Date startDate = fiscalPeriod.getStartDate();
        Date endDate = fiscalPeriod.getEndDate();
        double totalAbsenceHours = 0;

        ManageAbsencePersistence map = new ManageAbsencePersistence();
        for (Absence absence : map.getAbsencePerPersonOrdered(person, startDate, endDate)) {
            totalAbsenceHours += getHoursInDecimal(absence.getDurationHours());
        }

        return totalAbsenceHours;
    }

    private int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); // excluding end date

        return workDays;
    }

    @SuppressWarnings("deprecation")
    private double getHoursInDecimal(Date workingTime) {
        double hours = workingTime.getHours();
        hours += workingTime.getMinutes() / 60;
        return hours;
    }

}
