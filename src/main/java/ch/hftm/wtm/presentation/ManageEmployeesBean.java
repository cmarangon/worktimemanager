package ch.hftm.wtm.presentation;

import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;

import org.apache.log4j.Logger;

import ch.hftm.wtm.business.ManageEmployeesBusiness;
import ch.hftm.wtm.model.Person;

@SessionScoped
@ManagedBean(name = "manageEmployeesBean")
public class ManageEmployeesBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    protected Logger logger = Logger.getLogger(getClass());
    private ManageEmployeesBusiness meb = new ManageEmployeesBusiness();

    private Set<Person> allEmployees;
    private Person selectedPerson;
    private boolean isSelectedPersonNew = true;
    private boolean alteringEmploymentPeriod = false;

    public boolean isAlteringEmploymentPeriod()
    {
        return alteringEmploymentPeriod;
    }
    
    private UIComponent submitButton;

    public void setMybutton(UIComponent submitButton) {
        this.submitButton = submitButton;
    }

    public UIComponent getMybutton() {
        return submitButton;
    }
    
    public Set<Person> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(Set<Person> allEmployees) {
        this.allEmployees = allEmployees;
    }
    
    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public void setLoginBean(LoginBean lb)
    {
        this.loginBean = lb;
    }

    /**
     * On Load method, doing some initializing work
     * @author stu157
     * @return returns null
     */
    public String onLoad() {
        Person p = loginBean.getLoggedInPerson();
        
        selectedPerson = new Person();
       
        if(allEmployees == null)
            allEmployees = new HashSet<Person>();
        else
            allEmployees.clear();
        
        allEmployees.addAll(meb.getAllEmployees(p));
        
        return null;
    }

    /**
     * Button-Click-Event
     * @author stu157
     * @param employee Employee to modify
     */
    public void modifyEmployee(Person employee) {
        logger.debug("Selected Employee to modify: " + employee.getId());
        
        selectedPerson = employee;
        
        isSelectedPersonNew = false;
        alteringEmploymentPeriod = false;
    }
    
    /**
     * Button-Click-Event
     * @author stu157
     * @param employee Employee to delete
     */
    public void deactivateEmployee(Person employee) {
        try
        {
            System.out.println("selected Employee: " + employee);
    
            meb.deactivateEmployee(employee);
        }
        catch(Exception e)
        {
            MessageFactory.error("ch.hftm.wtm.VALIDATE_ABSENCE_UNEXPECTED_ERROR");
        }
        
        onLoad();
    }
    
    /**
     * @author stu157
     * Button-Click-Event
     */
    public void submit()
    {
        if(selectedPerson.getClient() == null)
            selectedPerson.setClient(loginBean.getLoggedInPerson().getClient());
        
        if(isSelectedPersonNew)
            submitNewEmployee();
        else
            submitEmployeeChanges();
        
        onLoad();
    }
    
    /**
     * resets the selectedPerson object.
     */
    public void newEmployee()
    {
        selectedPerson = new Person();
        isSelectedPersonNew = true;
        alteringEmploymentPeriod = false;
    }
    
    /**
     * @author stu157
     * Submits the new employee to the business logic.
     */
    private void submitNewEmployee()
    {
        meb.submitNewEmployee(selectedPerson);
    }
    
    /**
     * @author stu157
     * submits the changes on an employee to the business logic.
     */
    private void submitEmployeeChanges()
    {
        try { meb.submitEmployeeChanges(selectedPerson); }
        catch(Exception e) { MessageFactory.error("ch.hftm.wtm.MANAGE_EMPLOYEES_UNEXPECTED_ERROR"); }
    }
    
    /**
     * @author stu157
     * @param employee Employee to modify it's EmploymendPeriod
     */
    public void modifyEmploymentPeriod(Person employee)
    {
        selectedPerson = employee;
        alteringEmploymentPeriod = true;
    }
}
