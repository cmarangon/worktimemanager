package ch.hftm.wtm.business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;

import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.MasterData;
import ch.hftm.wtm.model.MonthlyBalance;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.ReportData;
import ch.hftm.wtm.model.ReportMasterData;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;
import ch.hftm.wtm.persistence.ManageAbsencePersistence;
import ch.hftm.wtm.persistence.StampingPersistence;

/**
 * @author Marcel Haldimann
 * @since 07.10.2016
 * @version 1.0
 */
public class ReportBusiness {

    // Dateformatter, für das formatieren der Datum
    private SimpleDateFormat dateFormatterTime = new SimpleDateFormat("HH.mm");
    private SimpleDateFormat dateFormatterDate = new SimpleDateFormat("EEEE, dd.MM.yyyy");
    DecimalFormat df = new DecimalFormat("####0.00");

    private static ReportBusiness instance;

    private static final int DEFAULT_BUFFER_SIZE = 10240;

    // Vaiable für die Istzeit
    private double actualTimeClass = 0;

    private List<Stamping> stampings = new ArrayList<>();
    private List<Absence> absences = new ArrayList<>();
    private List<ReportData> reportDatas = new ArrayList<>();
    private List<Person> employees = new ArrayList<>();

    public void setLoggedinPerson(Person loggedInPerson) {
        employees.addAll(loggedInPerson.getEmployees());
    }

    /**
     * Standardkontstruktor, private da Singleton
     */
    private ReportBusiness() {

    }

    /**
     * @param fromDate
     *            Von Datum
     * @param tillDate
     *            Bis Datum
     * @param loggedInPerson
     *            Aktuelle eingeloggte Person
     * @param employee
     *            Ausgewählte Person aus DropDown
     * @param pdfPath
     *            Pfad für das PDF
     * @return Generierter HTML-Code der für die Preview verwendet wird.
     */
    public String createReport(Date fromDate, Date tillDate, Person loggedInPerson, Person employee, String pdfPath) {
        Calendar newTillDate = Calendar.getInstance();
        newTillDate.setTime(tillDate);
        
        StampingPersistence sp = new StampingPersistence();
        ManageAbsencePersistence map = new ManageAbsencePersistence();
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();

        ReportMasterData rmd;
        ReportBuilder reportBuilder;

        // Alle Listen löschen wenn die Seite nicht neu geladen wurde:
        // Ausser die Employees Liste die bleibt gleich auch wenn die Seite
        // nicht akuallisiert wird
        stampings.clear();
        absences.clear();
        reportDatas.clear();

        // Alle Stempelungen holen und für den Report aufbereiten.
        // wenn ein mitarbeiter ausgewählt wurde, werden diese Stempelungen
        // geholt.
        // Ansonsten die eigenen
        if (employee == null) {
            stampings.addAll(sp.getAllStampingsByMonth(loggedInPerson, fromDate, tillDate));
            absences.addAll(map.findAbsenceListById(loggedInPerson.getId()));
        } else {
            stampings.addAll(sp.getAllStampingsByMonth(employee, fromDate, tillDate));
            absences.addAll(map.findAbsenceListById(employee.getId()));
        }

        // Daten Aufbereiten und abfüllen
        createReportDataList(fromDate, tillDate);

        if (employee == null) {
            rmd = createReportMasterData(loggedInPerson, epp.getActiveEmploymentPeriod(loggedInPerson.getId()),
                    fromDate, tillDate);
            reportBuilder = new ReportBuilder(reportDatas, rmd, pdfPath);
        } else {
            rmd = createReportMasterData(employee, epp.getActiveEmploymentPeriod(employee.getId()), fromDate, tillDate);
            reportBuilder = new ReportBuilder(reportDatas, rmd, pdfPath);
        }
        
        // ReportBuilder Objekt erstellen und liste mit Masterdaten
        // mitgeben.
        return reportBuilder.getReport();
    }

    /**
     * @param fromDate
     *            Von Datum
     * @param tillDate
     *            Bis Datum
     * @return Generierte Liste mi allen Daten für den Report
     */
    private List<ReportData> createReportDataList(Date fromDate, Date tillDate) {
        actualTimeClass = 0;
        Calendar start = Calendar.getInstance();
        start.setTime(fromDate);
        Calendar end = Calendar.getInstance();
        end.setTime(tillDate);

        // Iteriert vom Start bis zum Enddatum.
        // Erstellt für jeden Tag in der Liste ein ReportData Objekt.
        // Soll kein objekt erstellen wen Listen Leer.
        for (Date date = start.getTime(); start.before(end) || start.equals(end) ; start.add(Calendar.DATE, 1), date = start.getTime()) {
            StringBuffer absenceString = new StringBuffer();
            ReportData reportData = new ReportData();
            List<String> stampingStings = new ArrayList<String>();
            
            /*
             * Überprüft ob eine gerade Anzahl von Stempleungen vorhanden ist.
             * Füllt anschliessende alle Stemeplungen in eine Liste von String
             * Objekten
             */
            if (getStampingsByDate(date).size() % 2 == 0) {
                int counter = 0;

                Iterator<Stamping> iterator = getStampingsByDate(date).iterator();
                double workingHours = 0;
                while (iterator.hasNext()) {
                    Stamping stam1 = iterator.next();
                    Stamping stam2 = iterator.next();
                    Date come = stam1.getStampingTime();
                    Date go = stam2.getStampingTime();
                    workingHours += getDateDiff(come, go);
                }
                reportData.setWorktime("Total: " + df.format(workingHours / 60 / 60) + " Stunden");

                actualTimeClass += workingHours / 60 / 60;

                /*
                 * Iteriern über Liste von Stempelungen an einem Tag Schreibt
                 * einen Stempelfehler in die Liste wenn keine gerade Anazahl
                 * von Stelpelungen vorhanden ist.
                 */
                for (Stamping stamping : getStampingsByDate(date)) {
                    // Abfüllen de "kommen & gehen" Zeilen
                    if (counter % 2 == 0)
                        stampingStings.add("Kommen: " + dateFormatterTime.format(stamping.getStampingTime()));
                    else
                        stampingStings.add("Gehen:    " + dateFormatterTime.format(stamping.getStampingTime()));
                    counter++;
                }
            } else {
                stampingStings.add("Stempelfehler");
            }

            reportData.setStamping(stampingStings);
            reportData.setWorkingday(dateFormatterDate.format(date));

            for (Absence absence : getAbsenceByDate(date)) {
                if (getAbsenceByDate(date).size() > 1)
                    absenceString.append(absence.getComment() + ", ");
                else
                    absenceString.append(absence.getComment());
            }
            reportDatas.add(reportData);
        }

        // Liste nach Datum sortieren
        Collections.sort(reportDatas);

        return null;
    }

    /**
     * @param date
     *            Datum der gewünschten Stempleung
     * @return Liste aller Stempelungen an diesem Date
     */
    private List<Stamping> getStampingsByDate(Date date) {
        List<Stamping> list = new ArrayList<>();
        for (Stamping stamping : stampings) {
            if (DateUtils.isSameDay(date, stamping.getStampingTime())) {
                list.add(stamping);
            }
        }
        return list;
    }

    /**
     * @param date
     *            Datum der gewünschten Absence
     * @return Liste aller Ansenzen an diesem Date
     */
    private List<Absence> getAbsenceByDate(Date date) {
        List<Absence> list = new ArrayList<>();
        for (Absence absence : absences) {
            if (DateUtils.isSameDay(date, absence.getStart())) {
                list.add(absence);
            }
        }
        return list;
    }

    /**
     * @param person
     *            Person von der die Daten abgefüllt werden sollen.
     * @param employmentPeriod
     *            aktive EmploymentPeriod
     * @param fromDate
     *            von Datum
     * @param tillDate
     *            bis Datum
     * @return abgefülltes ReportMasterData Objekt
     */
    private ReportMasterData createReportMasterData(Person person, EmploymentPeriod employmentPeriod, Date fromDate,
            Date tillDate) {
        ReportMasterData reportMasterData = new ReportMasterData();

        // Istzeit holen
        Double actualTime = round(actualTimeClass, 2, BigDecimal.ROUND_HALF_UP);
        // SollZeit holen
        Double toTime =  calculateToTime(fromDate, tillDate, person);
        
        // Holt sich den letzten abgeschlossenen Monat
        reportMasterData.setHolidayBalanceOld(getCalculatedVacationDays(person, tillDate));

        // Istzeit und Sollzeit setzen
        reportMasterData.setActualTime(actualTime);
        reportMasterData.setToTime(toTime);

        reportMasterData.setFromDate(fromDate);
        reportMasterData.setTillDate(tillDate);
        reportMasterData.setLevelOfEmployment(employmentPeriod.getEmploymentLevelPercent());
        // Istzeit - Sollzeit
        reportMasterData.setOvertime(round(actualTime-toTime, 2, BigDecimal.ROUND_HALF_UP));  
        reportMasterData.setPerson(person);

        return reportMasterData;
    }

    /**
     * @param fromDate
     *            Von Datum
     * @param tillDate
     *            Bis Datum
     * @param person
     *            Person von der die Zeit berechnet werden soll
     * @return double die Berechnete Zeit
     */
    private Double calculateToTime(Date fromDate, Date tillDate, Person person) {
        List<EmploymentPeriod> employmentPeriod = person.getEmploymentPeriods();
        Double hours = 0.0;

        Calendar start = Calendar.getInstance();
        start.setTime(fromDate);
        Calendar end = Calendar.getInstance();
        end.setTime(tillDate);

        // Iteriert über alle
        for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);
            for (EmploymentPeriod ep : employmentPeriod) {
                // Ist der Tag ein Tag am Wochenende.
                if (calDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                        && calDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {

                    // In welcher EmmploymentPeriod ist das Datum
                    if (ep.getValidTo() == null) {
                        Date actualDate = new Date();

                        if (date.before(actualDate) && date.after(ep.getValidFrom())) {
                            hours += getHoursInDecimalWithEmploymnetLevel(ep.getDailyWorkingHours(),
                                    ep.getEmploymentLevelPercent());
                        }

                    } else {
                        if (date.before(ep.getValidTo()) && date.after(ep.getValidFrom())) {
                            hours += getHoursInDecimalWithEmploymnetLevel(ep.getDailyWorkingHours(),
                                    ep.getEmploymentLevelPercent());
                        }
                    }
                }
            }
        }

        return hours;

    }

    /**
     * @param workingTime
     *            Arbeitszeit
     * @param employmentLevel
     *            Prozent der Anstellung
     * @return Stunden in Dezimalwert
     */
    @SuppressWarnings("deprecation")
    private double getHoursInDecimalWithEmploymnetLevel(Date workingTime, double employmentLevel) {
        double hours = workingTime.getHours() * employmentLevel / 100;
        hours += workingTime.getMinutes() / 60 * employmentLevel / 100;
        return hours;
    }

    /**
     * @param employmentPeriodList
     * @return active employmentPeriod
     */
    private EmploymentPeriod getActivEmploymentPeriod(Person person) {
        EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
        return epp.getActiveEmploymentPeriod(person.getId());
    }

    /**
     * @param monthlyBalanceList
     *            liste aller MonthlyBalanceList
     * @param date
     *            mit Monat von welchem die MonthlyBalance geholt werden soll
     * @return Letzte MonthlyBalance
     */
    private MonthlyBalance getLastMonthlyBalance(List<MonthlyBalance> monthlyBalanceList, Date date) {
        DateUtils.addMonths(date, -1);
        // Ermittelt den letzten MonthlyBalance
        for (MonthlyBalance mb : monthlyBalanceList) {
            if (getMonthFromDate(mb.getTimeFrame()) == getMonthFromDate(date)) {
                return mb;
            } else
                return null;
        }
        return null;
    }

    /**
     * @param person
     *            von der Die Ferien geholt werden sollen
     * @param tillDate
     * @return Anzahl der Übrigen Ferientage
     */
    private Double getVacationDays(Person person, Date tillDate) {
        EmploymentPeriod aep = getActivEmploymentPeriod(person);
        MonthlyBalance mb = getLastMonthlyBalance(aep.getMonthlyBalances(), tillDate);

        // Sollte der mitarbeiter neu sein und noch kein abgesclossener
        // Monat vorhanden sein, werden die Ferien berechnet.
        if (mb != null) {
            // Holt die monthly balance für den letzten abgeschlossenen Monat
            return mb.getVacationDaysBalance();
        } else {
            return getCalculatedVacationDays(person, tillDate);
        }
    }

    /**
     * @param person
     *            person von der die Ferienzeit berechnet werden soll
     * @param tillDate
     *            bis Datum
     * @return gibt die Ferientage zurück die der Mitarbeiter
     */
    private Double getCalculatedVacationDays(Person person, Date tillDate) {
        List<MasterData> mdl = person.getClient().getMasterDatas();
        /*
         * Aktuelle MasterData holen und die gesetzten Ferientage holen Die
         * Ferien werden berechnet nach Monat.
         */
        for (MasterData masterData : mdl) {
            if (masterData.isActive()) {
                return Double.valueOf(masterData.getDefaultAnnualVacationDays() / 12 * getMonthFromDate(tillDate));
            }
        }
        return null;
    }

    /**
     * @param date
     *            Datum von dem der monat angezeigt werden soll
     * @return Montat als integer (Januar = 1)
     */
    private int getMonthFromDate(Date date) {
        // Mit calendar gelöst
        // +1 weil Januar == 0
        // DATE und Calendar in Java ist eine Katastrophe.............
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * @param date1
     *            Erstes Datum
     * @param date2
     *            Zewites Dautm
     * @param timeUnit
     *            Welcher Unit soll die Differnez zurückgegeben werden
     * @return long differenz.
     */
    private Long getDateDiff(Date date1, Date date2) {
        return (date2.getTime() / 1000) - (date1.getTime() / 1000);
    }

    // Publuic Methoden

    /**
     * @return Instanz von ReportBusiness
     */
    public static ReportBusiness getInstance() {
        if (instance == null) {
            instance = new ReportBusiness();
        }
        return instance;
    }

    /**
     * Wird für den PersonConverter benötigt
     *
     * @param eMail
     *            der Person
     * @return die dazugehörige Person
     */
    public Person getEmployee(String eMail) {
        for (Person person : employees) {
            if (person.getEmail().equals(eMail)) {
                return person;
            }
        }
        return null;
    }

    /**
     * @param unrounded
     *            Ungerundet
     * @param precision
     *            Anzahl Nachkommastellen
     * @param roundingMode
     *            Rundungs Modus
     * @return
     */
    private double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    /**
     * @param destination
     *            Ort an dem das File gespeichert werden soll
     * @return Navigationsstring
     */
    public String executeDownload(String destination) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        File file = new File(destination);
        try {
            if (!file.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return "report.xhtml";
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } catch (Exception e) {

        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        context.responseComplete();
        return null;
    }
}