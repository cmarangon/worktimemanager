package ch.hftm.wtm.business;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.persistence.ManageAbsencePersistence;

/**
 * @author srdjankovacevic
 * @since 31.07.2016
 * @version 1.0
 */
public class ManageAbsenceBusiness {

    private ManageAbsencePersistence manageAbsencePersistence = new ManageAbsencePersistence();
    private Date startDate = new Date();
    private Date endDate = new Date();
    private CalculateAbsenceTime cat = new CalculateAbsenceTime(startDate, endDate);
    private Calendar calendarStartdate = Calendar.getInstance();
    private Calendar calendarEndedate = Calendar.getInstance();

    /**
     * Diese Methode überprüft, ob das Enddatum grösser als das Startdatum ist.
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @return
     */
    public boolean checkIfEndDateIsLargerStartDate(Date startDate, Date endDate) {
        if (endDate.after(startDate)) {
            // Ist das Start und Enddatum am selben Tag?
            checkIfSameDay(startDate, endDate);
            return true;
        } else {
            System.out.println("Enddatum ist vor Startdatum -> Fehler");
            return false;
        }
    }

    /**
     * Diese Methode überprüft, ob das Start- und Enddatum am selben Tag sind
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean checkIfSameDay(Date startDate, Date endDate) {

        calendarStartdate.setTime(startDate);
        calendarEndedate.setTime(endDate);

        // Ist der Starttag und der Enddtag der selbe?
        if (calendarStartdate.get(Calendar.YEAR) == calendarEndedate.get(Calendar.YEAR)) {
            if (calendarStartdate.get(Calendar.MONTH) == calendarEndedate.get(Calendar.MONTH)) {
                if (calendarStartdate.get(Calendar.DAY_OF_WEEK) == calendarEndedate.get(Calendar.DAY_OF_WEEK)) {
                    // Berechne Zeit zwischen den eingegeben Daten
                    cat.calculateTimeBetweenStartAndEndDate(startDate, endDate);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Hole die Absenzen aus der der Datenbank, welche der entsprechenden Person zugeordnet sind
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param id
     * @return
     */
    public List<Absence> getAbsenceListById(Long id) {
        List<Absence> absensceList = manageAbsencePersistence.findAbsenceListById(id);
        return absensceList;
    }

    /**
     * Hole die Absenz aus der Datenbank, welche vom Benutzer angewählt wurde (Absenz wird editiert)
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param id
     * @return
     */
    public Absence getAbsenceById(Long id) {
        Absence absence = manageAbsencePersistence.findAbsenceById(id);
        return absence;
    }

    /**
     * Diese Methode erstellt
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     */
    public void createAbsence(String comment, Calendar durationHours, Date startDate, Date endDate, AbsenceType at,
                              EmploymentPeriod ep) {

        // Neue Absenz erstellen
        Absence absence = new Absence();
        absence.setComment(comment);
        absence.setDurationHours(durationHours.getTime());
        absence.setStart(startDate);
        absence.setEnd(endDate);
        absence.setAssignedAbsenceType(at);
        absence.setPerson(ep);
        saveAbsence(absence);
        
    }

    /**
     * Speichere die editierte Absenz
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absence
     */
    public void editAbsence(Absence absence) {
        absence.setIsVerified(false); // Verified wieder auf false setzen
        manageAbsencePersistence.makePersistentByMerge(absence);
    }

    /**
     * Speichere die neue Absenz
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absence
     */
    public void saveAbsence(Absence absence) {
        manageAbsencePersistence.saveAbsence(absence);
    }

    /**
     * Lösche die Absenz, die sich in der Zukunft befindet
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absence
     * @return
     */
    public boolean deleteAbsenceInFuture(Absence absence) {
        if (absence.getStart().before(Calendar.getInstance().getTime())) {
            System.out.println("Das Datum der Absence ist vor dem heutigen Datum! -> Kann nicht gelöscht werden");
            return false;
        } else {
            manageAbsencePersistence.makeTransient(absence);
            System.out.println("Das Datum der Absence ist in der Zukunft -> löschen");
            return true;
        }
    }
}
