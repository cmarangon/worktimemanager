package ch.hftm.wtm.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.hftm.wtm.business.ReportBusiness;
import ch.hftm.wtm.model.Person;

@SessionScoped
@ManagedBean(name = "reportBean")
public class ReportBean {

    // ManagedProperty für das Holen der eingeloggten Person.
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    // KlassenAttribute
    private Date fromDate = new Date();
    private Date tillDate = new Date();
    // Aktuell eingeloggte person
    private Person currentUser;
    // Ausgewählte Person
    private Person chosenUser;

    private String htmlPreview;

    // Liste aller Unterschtelten Mitarbeiter
    private List<Person> employeeList = new ArrayList<>();

    public String onLoad() {
        ReportBusiness reportBusiness;
        getCurrentUser();
        reportBusiness = ReportBusiness.getInstance();
        reportBusiness.setLoggedinPerson(currentUser);
        return null;
    }

    // Buttons GetReport
    /**
     * @return Navigation
     */
    public String generateReport(){
        ReportBusiness reportBusiness = ReportBusiness.getInstance();
        if (fromDate.before(tillDate)) {
            htmlPreview = reportBusiness.createReport(fromDate, tillDate, currentUser, chosenUser, pdfPath());
            reportBusiness.executeDownload(pdfPath());
        } else
            MessageFactory.error("ch.hftm.wtm.report.ENDDATE_BEFORE_STARTDATE");

        return "report.xhtml";
    }

    /**
     * @return gibt die Preview zurück
     */
    public String generatePreview() {
        ReportBusiness reportBusiness = ReportBusiness.getInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getApplication().evaluateExpressionGet(context, "#{resource['style:css/images/wtm_icon_small.png']}", String.class);
        if (fromDate.before(tillDate)) {
            htmlPreview = reportBusiness.createReport(fromDate, tillDate, currentUser, chosenUser, pdfPath());
            htmlPreview = htmlPreview.replace("src=\"\"",
                    "src=\"" + url + "\"");
        } else
            MessageFactory.error("ch.hftm.wtm.ENDDATE_BEFORE_STARTDATE");
        return "report.xhtml";
    }

    
    /**
     * @return Pfad wo das PDF vor dem Downlaod abgelegt wird.
     */
    private String pdfPath() {
        String path;
        if (chosenUser == null) {
            path = System.getProperty("user.home") + "/temp/" + "Report" + currentUser.getFirstName() + currentUser.getLastName() + currentUser.getId()
                    + ".pdf";
        } else {
            path = System.getProperty("user.home") + "/temp/" + "Report" + chosenUser.getFirstName() + chosenUser.getLastName() + chosenUser.getId()
                    + ".pdf";
        }
        return path;
    }

   
    /**
     * Setzt die eingeloggte Person
     */
    private void getCurrentUser() {
        // Eingeloggte Person holen
        currentUser = loginBean.getLoggedInPerson();

        if (currentUser.getEmployees().isEmpty() == false) {
            employeeList.clear();
            employeeList.addAll(currentUser.getEmployees());
        }
    }

    // GETTER AND SETTER
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getTillDate() {
        return tillDate;
    }

    public void setTillDate(Date tillDate) {
        this.tillDate = tillDate;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public List<Person> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Person> employeeList) {
        this.employeeList = employeeList;
    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }

    public Person getChosenUser() {
        return chosenUser;
    }

    public void setChosenUser(Person chosenUser) {
        this.chosenUser = chosenUser;
    }

    public String getHtmlPreview() {
        return htmlPreview;
    }

    public void setHtmlPreview(String htmlPreview) {
        this.htmlPreview = htmlPreview;
    }
}