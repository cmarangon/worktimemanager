package ch.hftm.wtm.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hftm.wtm.exception.DatesMismatchException;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;
import ch.hftm.wtm.persistence.StampingPersistence;

/**
 *
 * @author unknown
 */
public class StampingBusiness {

    private StampingPersistence sp = new StampingPersistence();

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Stamp#stamp(ch.hftm.wtm.model.EmploymentPeriod, ch.hftm.wtm.model.Person)
     */
    public void stamp(EmploymentPeriod emPeriod, Person loggetPerson) {

        System.out.println("In Methode Stamp");

        // Neue Stamping anlegen
        ch.hftm.wtm.model.Stamping newStamping = new Stamping();

        // Hole und speichere den aktuellen ZeitStempel
        newStamping.setStampingTime(new Date());

        // Verbinde die Stempelung mit aktive EmploymentPeriod der Person
        newStamping.setAssignedPerson(emPeriod);
        System.out.println("Wurde verknüpft");
        newStamping.setVerificationStatus("Verifiziert");

        // Verbinde die Stempelung mit der Person
        newStamping.setApprovedBy(loggetPerson);

        // sp.makePersistent(newStamping);
        sp.saveStamping(newStamping); // Neue Methode, Transaction muss nun manuell gestartet werden (ist in der Generic
                                      // Persistence nicht mehr vorhanden)

        // ******************************************************************************
        // **** Debuginfos **************************************************************
        // ******************************************************************************
        System.out.println("Info zur Stempelung:\n" + "=====================");
        // Hole die Zeit der Stempelung
        Date tempStemp = newStamping.getStampingTime();
        System.out.println("Zeit: " + tempStemp);
        // ******************************************************************************
        // ******************************************************************************
    }

    /**
     * Hole alle Stempelungen
     *
     * @return
     */
    public List<Stamping> getAllStamps() {

        System.out.println("Hole alle Stempelungen");
        List<Stamping> stampingListe = sp.findAll();

        for (Stamping s : stampingListe) {

            System.out.println("****************************************");
            // ID der Stempelung
            System.out.println("ID Stempelung :" + s.getId());

            // Zeitstempel
            Date tempStamp = s.getStampingTime();
            System.out.println("Zeit: " + tempStamp);
            // Kommentar der Stempelung
            System.out.println("Kommentar :" + s.getComment());
            System.out.println("****************************************");

        }

        return null;
    }

    public List<ch.hftm.wtm.model.Stamping> getStamps(Date fromDate, Date untilDate, EmploymentPeriod emPeriod) {

        // Hole die Stempelungen von dem angegeben Zeitpunkt aus der Datenbank

        // String zusammenstellen für Abfrage
        // Beispiel: String attribute = "stampingtime between '2016-08-21 00:00:00' AND '2016-08-21 23:59:59' AND
        // ASSIGNEDPERSON = 99";
        // **********************************************************
        // Format bestimmen

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        // Datum Formatieren für fromDate
        String formattedFrom = format1.format(fromDate.getTime());
        System.out.println("Formatiert From:" + formattedFrom);

        // Datum Formatieren für untilDate
        String formattedUntil = format1.format(untilDate.getTime());
        System.out.println("Formatiert Until:" + formattedUntil);

        // String zusammenführen
        String attribute = "stampingtime between '" + formattedFrom + " " + "00:00:00' " + "AND '" + formattedUntil
                + " " + "23:59:59' " + "AND " + "assignedperson = " + emPeriod.getId();
        // **********************************************************
        System.out.println("attribute :" + attribute);

        // Abfrage in der Datenbank
        List<Stamping> stampingListe = sp.makeQuerySearchByString1(attribute);

        return stampingListe;
    }

    /**
     * @param person
     * @return
     */
    public Stamping getLastStamp(Person person) {
        Stamping lastStamp = null;

        StampingPersistence sp = new StampingPersistence();
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        EmploymentPeriod activeEmploymentPeriod = epp.getActiveEmploymentPeriod(person.getId());
        try {
            if (activeEmploymentPeriod != null)
                for (Stamping stamp : sp.getAllStampingsByDate(activeEmploymentPeriod,
                        new Date()))
                    if (lastStamp == null || lastStamp.getStampingTime().before(stamp.getStampingTime()))
                        lastStamp = stamp;
        } catch (DatesMismatchException e) {
            System.out.println("Aktive employmentPeriod ist nicht für heute gültig.");
        }

        return lastStamp;
    }

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Stamp#getEditStamping(ch.hftm.wtm.model.Stamping, ch.hftm.wtm.model.Person)
     */
    public void getEditStamping(Stamping toEditStamping1, Person id) {

        //

        // Lade die Daten der Stempelung

    }

    // Speichere die neuen Stempelungen ab, falls die Stempelung mehr als 48h zurückliegt, müssen die noch verifiziert
    // werden.¨
    // Die alten Stempelungen werden noch nicht gelöscht, bis diese Verifiziert worden sind.
    public void setEditStamping(ch.hftm.wtm.model.Stamping toEditStamping1, Person id) {

        // Liegt die zu korrigierende Stempelung mehr als 48h zurück?
        // Wenn ja, kennzeichnen, dass die Korrektur verifiziert werden muss.

        System.out.println("In Methode Stamp");

        // Speichere die Stempelung in die Datenbank
        /// sp.makePersistent(changeStamping);
        sp.updateStamping(toEditStamping1, id);

    }

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Stamp#deleteStamping(ch.hftm.wtm.model.Stamping, ch.hftm.wtm.model.Person)
     */
    public void deleteStamping(Stamping toDeleteStamping1, Person id) {

        // Lösche die beiden Stempelungseinträge aus der Datenbank

    }

    /*
     * (non-Javadoc)
     * @see ch.hftm.wtm.interfaces.Stamp#isUserStamptIn(ch.hftm.wtm.model.Person)
     */
    public boolean isUserStamptIn(Person person) {
        if (person == null)
            return false;

        List<Stamping> stampingsToday = new ArrayList<>();

        StampingPersistence sp = new StampingPersistence();
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        EmploymentPeriod activeEmploymentPeriod = epp.getActiveEmploymentPeriod(person.getId());
        try {
            if (activeEmploymentPeriod != null)
                stampingsToday.addAll(sp.getAllStampingsByDate(activeEmploymentPeriod,
                        new Date()));
        } catch (DatesMismatchException e) {
            System.out.println("Aktive employmentPeriod ist für heute nicht gültig.");
        }
        return stampingsToday.size() % 2 != 0;
    }

}
