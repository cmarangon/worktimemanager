package ch.hftm.wtm.presentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ch.hftm.wtm.business.CalculateAbsenceTime;
import ch.hftm.wtm.business.ManageAbsenceBusiness;
import ch.hftm.wtm.business.ManageAbsenceTypeBusiness;
import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;

/**
 *
 * @author srdjankovacevic
 * @since 08.08.2016
 * @version 1.0
 */
@ManagedBean(name = "manageAbsenceBean")
@SessionScoped
public class ManageAbsenceBean {

    public ManageAbsenceBean() {
        System.out.println("manageAbsenceBean=" + Thread.currentThread().getId());
    }

    // Diese Methode wird beim Laden der Seite aufgerufen
    public String onLoad() {
        System.out.println("Onload manageAbsenceBean ausgeführt!");
        getAbsenceTypeByClientId();
        getAbsenceListById();
        return null;
    }

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 8689800260335765619L;
    private String comment = "";
    private Date startDate = new Date();
    private Date endDate = new Date();
    private Date durationHours = new Date();

    private List<AbsenceType> absenceTypeList = new ArrayList<AbsenceType>();
    private List<Absence> absenceList = new ArrayList<Absence>();
    private AbsenceType at = new AbsenceType();
    private CalculateAbsenceTime cat = new CalculateAbsenceTime(startDate, endDate);
    private Absence foundAbsence;

    // Bean Incection
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Date durationHours) {
        this.durationHours = durationHours;
    }

    public List<AbsenceType> getAbsenceTypeList() {
        return absenceTypeList;
    }

    public void setAbsenceTypeList(List<AbsenceType> absenceTypeList) {
        this.absenceTypeList = absenceTypeList;
    }

    public List<Absence> getAbsenceList() {
        return absenceList;
    }

    public void setAbsenceList(List<Absence> absenceList) {
        this.absenceList = absenceList;
    }

    public AbsenceType getAt() {
        return at;
    }

    public void setAt(AbsenceType at) {
        this.at = at;
    }

    public CalculateAbsenceTime getCat() {
        return cat;
    }

    public void setCat(CalculateAbsenceTime cat) {
        this.cat = cat;
    }

    public String selectRow(Absence selectedAbsence) {
        foundAbsence = getAbsenceById(selectedAbsence);
        return "editAbsence";
    }

    public Absence getFoundAbsence() {
        return foundAbsence;
    }

    public void setFoundAbsence(Absence foundAbsence) {
        this.foundAbsence = foundAbsence;
    }

    /**
     * Erstelle die erzeugte Absenz
     *
     * @author srdjankovacevic
     * @since 04.09.2016
     * @return
     */
    public String createAbsence() {
        System.out.println("schnauze");
        // Hole die aktive EmploymentPeriod
        EmploymentPeriod ep = getActiveEmploymentPeriod();
        if (ep != null) {

            // Instanz der Businessklassen erstellen
            ManageAbsenceBusiness mab = new ManageAbsenceBusiness();

            // Prüfen, ob das Enddatum nach dem Startdatum kommt
            if (mab.checkIfEndDateIsLargerStartDate(startDate, endDate)) {

                // Prüfen, ob das Start- und Enddatum am selben Tag ist.
                if (mab.checkIfSameDay(startDate, endDate)) {

                    // Berechne die Zeit zwischen Start- und Enddatum
                    cat.calculateTimeBetweenStartAndEndDate(startDate, endDate);

                    // Setze die Stunden und Minuten (wird auf der .xhtml Seite angezeigt)
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, (int) cat.getDiffHours());
                    calendar.set(Calendar.MINUTE, (int) cat.getDiffMinutes());
                    calendar.set(Calendar.SECOND, 0);

                    // Absenz erstellen
                    mab.createAbsence(comment, calendar, startDate, endDate, at, ep);

                    // Absenzen holen um sie in der View "overviewAbsence" anzeigen zu lassen
                    getAbsenceListById();
                    return "overviewAbsence";
                } else {
                    MessageFactory.error("ch.hftm.wtm.absence.NOT_SAME_DAY");
                }
            } else {
                // Fehler: Das Enddatum befindet sich vor dem Startdatum
                MessageFactory.error("ch.hftm.wtm.absence.ENDDATE_BEFORE_STARTDATE");
            }
        }
        return null;
    }

    /**
     * Man kann die Absence aus der overViewAbsence bearbeiten
     *
     * @author srdjankovacevic
     * @since 04.09.2016
     * @return
     */
    public String editAbsence() {
        // Instanz der Businessklassen erstellen
        ManageAbsenceBusiness mab = new ManageAbsenceBusiness();

        // Prüfen, ob das Start- und Enddatum am selben Tag ist.
        if (mab.checkIfSameDay(foundAbsence.getStart(), foundAbsence.getEnd())) {

            // Editierte Absenz speichern
            mab.editAbsence(foundAbsence);

            // Rufe die Methode nochmal auf, sonst wird die View nicht aktualisiert...
            getAbsenceListById();

            return "overviewAbsence";

        } else {
            // Fehler: Das Start- und Enddatum sind nicht am selben Tag!
            MessageFactory.error("ch.hftm.wtm.NOT_SAME_DAY");
        }
        return null;
    }

    /**
     * Hole die Absenztypen des entsprechenden Clients (Absenztyp wird bei der Absenzerstellung ausgewählt)
     *
     * @author srdjankovacevic
     *
     * @since 04.09.2016
     * @return
     */
    public String getAbsenceTypeByClientId() {
        ManageAbsenceTypeBusiness matb = new ManageAbsenceTypeBusiness();
        absenceTypeList = matb.getAbsenceTypeByClientId(loginBean.getClientFromLoggedInPerson().getId());
        return null;
    }

    /**
     * Hole die Absenzliste des entsprechenden Mitarbeiters für die Übersicht
     *
     * @author srdjankovacevic
     * @since 04.09.2016
     * @return
     */
    public String getAbsenceListById() {
        ManageAbsenceBusiness mab = new ManageAbsenceBusiness();
        absenceList = mab.getAbsenceListById(loginBean.getLoggedInPerson().getId());
        return null;
    }

    /**
     * Hole die entsprechende Absenz für die Bearbeitung
     *
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absence
     * @return
     */
    public Absence getAbsenceById(Absence absence) {
        ManageAbsenceBusiness mab = new ManageAbsenceBusiness();
        absence = mab.getAbsenceById(absence.getId());
        return absence;
    }

    public EmploymentPeriod getActiveEmploymentPeriod() {

        // Hole die Aktive EmploymentPeriods aus der Datenbank
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        EmploymentPeriod ep = epp.getActiveEmploymentPeriod(loginBean.getLoggedInPerson().getId());
        System.out.println("nach querry");

        if (ep != null && ep.isActive()) {
            return ep;
        } else {
            MessageFactory.error("ch.hftm.wtm.absence.NO_ACTIVE_EMPLOYMENTPERIOD");
            return ep = null;
        }
    }

    /**
     * Lösche die Absenz, wenn sie in der Zukunft liegt.
     *
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absence
     * @return
     */
    public String deleteAbsence(Absence absence) {
        ManageAbsenceBusiness mab = new ManageAbsenceBusiness();

        if (mab.deleteAbsenceInFuture(getAbsenceById(absence))) {
            getAbsenceListById();
            return null;
        } else {
            MessageFactory.error("ch.hftm.wtm.absence.DELETED_ABSENCE_NOT_IN_FUTURE");
            return null;
        }
    }

    public String goToAbsenceOverview() {
        // How can you do something stupid?
        getAbsenceListById();
        return "overviewAbsence";
    }
}