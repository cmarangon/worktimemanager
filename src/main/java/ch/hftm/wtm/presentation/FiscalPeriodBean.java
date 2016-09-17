package ch.hftm.wtm.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ch.hftm.wtm.business.FinalStatementBusiness;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.FiscalPeriod;
import ch.hftm.wtm.model.Person;

/**
 *
 * @author HOEFI
 * @since 08.08.2016
 * @version 1.0
 */
@ManagedBean(name = "fiscalPeriodBean")
@SessionScoped
public class FiscalPeriodBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private FiscalPeriod selectedFiscalPeriod;
    private List<FiscalPeriodPersonHelper> personDetails = new ArrayList<FiscalPeriodPersonHelper>();

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public FiscalPeriod getSelectedFiscalPeriod() {
        return selectedFiscalPeriod;
    }

    public void setSelectedFiscalPeriod(FiscalPeriod selectedFiscalPeriod) {
        this.selectedFiscalPeriod = selectedFiscalPeriod;
    }

    public List<FiscalPeriodPersonHelper> getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(List<FiscalPeriodPersonHelper> personDetails) {
        this.personDetails = personDetails;
    }
    
    public boolean getHasValidStampings() {
        for (FiscalPeriodPersonHelper fpph : personDetails) {
            if (!fpph.isHasValidStampings())
                return false;
        }
        return true;
    }

    public List<FiscalPeriod> getFiscalPeriods() {
        List<FiscalPeriod> result = new ArrayList<FiscalPeriod>();
        Client client = loginBean.getClientFromLoggedInPerson();
        if (client == null) return result;
        for (FiscalPeriod fp : client.getFiscalPeriods()) {
            if (fp.getCompletedOn() == null)
                result.add(fp);
        }
        return result;
    }

    public String showEmployeeOverview() {
        Client client = loginBean.getClientFromLoggedInPerson();
        if (client == null) return "";

        FinalStatementBusiness fsb = new FinalStatementBusiness();
        long ms = System.currentTimeMillis();
        fsb.checkStampings(client, selectedFiscalPeriod);
        ms = System.currentTimeMillis() - ms;
        System.out.println("CheckStampings (in ms): " + ms);
        Set<Person> employeesOk = fsb.getPersonsWithCorrectStampings();
        Set<Person> employeesNotOk = fsb.getPersonsWithInvalidStampings();

        personDetails.clear();
        fillPersonDetails(employeesNotOk, false);
        fillPersonDetails(employeesOk, true);
        
        if (!employeesNotOk.isEmpty())
            MessageFactory.error("ch.hftm.wtm.business.fiscalperiod.INCONSISTENT_STAMPINGS");

        return "closeFiscalPeriodEmployees";
    }

    public String backToSelection() {
        selectedFiscalPeriod = null;
        personDetails.clear();
        return "closeFiscalPeriod.xhtml";
    }

    public String closePeriod() {
        for (FiscalPeriodPersonHelper fpph : personDetails) {
            if (!fpph.isHasValidStampings())
                return "";
        }
        FinalStatementBusiness fsb = new FinalStatementBusiness();
        long ms = System.currentTimeMillis();
        fsb.closePeriod(loginBean.getClientFromLoggedInPerson(), selectedFiscalPeriod);
        ms = System.currentTimeMillis() - ms;
        System.out.println("ClosePeriod (in ms): " + ms);
        return "closeFiscalPeriod";
    }

    private void fillPersonDetails(Set<Person> persons, boolean hasValidStampings) {
        FinalStatementBusiness fsb = new FinalStatementBusiness();
        for (Person p : persons) {
            FiscalPeriodPersonHelper fpph = new FiscalPeriodPersonHelper();
            fpph.setPerson(p);
            fpph.setWorkedHours(fsb.calculateWorkedHoursPerPerson(p, selectedFiscalPeriod));
            fpph.setWorkingHours(fsb.calculateWorkingHourPerPerson(p, selectedFiscalPeriod));
            fpph.setHasValidStampings(hasValidStampings);
            personDetails.add(fpph);
        }
    }
}
