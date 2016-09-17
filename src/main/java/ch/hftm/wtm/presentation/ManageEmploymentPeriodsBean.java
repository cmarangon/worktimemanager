
package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;

import ch.hftm.wtm.business.ManageEmploymentPeriodsBusiness;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;

@ManagedBean(name = "manageEmploymentPeriodsBean")
@SessionScoped
public class ManageEmploymentPeriodsBean implements Serializable {
    
    private static final long serialVersionUID = 1123231L;
    protected Logger logger;
    private Set<EmploymentPeriod> allEmploymentPeriods = new HashSet<EmploymentPeriod>();
    private EmploymentPeriod selectedEmploymentPeriod = new EmploymentPeriod();
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean; 
    
    @ManagedProperty(value = "#{manageEmployeesBean}")
    private ManageEmployeesBean manageEmployeesBean; 

    public EmploymentPeriod getSelectedEmploymentPeriod()
    {
        return selectedEmploymentPeriod;
    }
    
    public void setSelectedEmploymentPeriod(EmploymentPeriod selectedEmploymentPeriod)
    {
        this.selectedEmploymentPeriod = selectedEmploymentPeriod;
    }

    public Set<EmploymentPeriod> getAllEmploymentPeriods() {
        return allEmploymentPeriods;
    }

    public void setAllEmploymentPeriods(Set<EmploymentPeriod> allEmploymentPeriods) {
        this.allEmploymentPeriods = allEmploymentPeriods;
    }
    
    public void setLoginBean(LoginBean loginBean)
    {
        this.loginBean = loginBean;
    }
    
    public void setManageEmployeesBean(ManageEmployeesBean manageEmployeesBean)
    {
        this.manageEmployeesBean = manageEmployeesBean;
    }
    
    public void newEmploymentPeriod()
    {
        selectedEmploymentPeriod = new EmploymentPeriod();
    }

    /**
     * Called on load, doing some initialization work
     * @author stu157
     * @return
     */
    public String onLoad()
    {
        System.out.println("On Load ManageEmploymentPeriods");
        
        try
        {
            Person p = manageEmployeesBean.getSelectedPerson();
            System.out.println(p);
            
            if(allEmploymentPeriods.size() > 0)
                allEmploymentPeriods.clear();
            
            ManageEmploymentPeriodsBusiness mep = new ManageEmploymentPeriodsBusiness();
            allEmploymentPeriods = mep.getAllEmploymentPeriods(p);
            EmploymentPeriodPersistence epp = new EmploymentPeriodPersistence();
            selectedEmploymentPeriod = epp.getActiveEmploymentPeriod(p.getId());
        }
        catch(NullPointerException npe)
        {
            System.out.println("Person is null");
            return null;
        }
        
        return null;
    }
    
    private void loadAllEmploymentPeriods(Person p)
    {
        //allEmploymentPeriods.addAll(p.getEmploymentPeriods());
        for(EmploymentPeriod ep : p.getEmploymentPeriods())
        {                
            EmploymentPeriod newEp = new EmploymentPeriod();
            newEp.setId(ep.getId());
            newEp.setEmployee(ep.getEmployee());
            newEp.setActive(ep.isActive());
            newEp.setEmploymentLevelPercent(ep.getEmploymentLevelPercent());
            newEp.setDailyWorkingHours(ep.getDailyWorkingHours());
            newEp.setMaxOvertime(ep.getMaxOvertime());
            newEp.setValidFrom(ep.getValidFrom());
            newEp.setValidTo(ep.getValidTo());
            
            allEmploymentPeriods.add(newEp);
        }
        
    }
    
    /**
     * @author stu157
     * refreshes the values of the selected EmploymentPeriod on a change of the DropDown-selection 
     */
    public String listener(AjaxBehaviorEvent ev) 
    {
        System.out.println("listener");

        if(selectedEmploymentPeriod == null)
        {
            selectedEmploymentPeriod = new EmploymentPeriod();
            System.out.println("New EmploymentPeriod.");
        }
        else
        {
            System.out.println("Selected EmploymentPeriod Id: " + selectedEmploymentPeriod.getId());
        }
        
        
        return null;
    }
    
    /**
     * @author stu157
     * Submits the changes from the form to the database.
     */
    public void submit()
    {
        System.out.println("Submit selected ep Id: " + selectedEmploymentPeriod.getId());
        
        ManageEmploymentPeriodsBusiness mepb = new ManageEmploymentPeriodsBusiness();
        mepb.submitChanges(selectedEmploymentPeriod);
        
        allEmploymentPeriods.clear();
        loadAllEmploymentPeriods(manageEmployeesBean.getSelectedPerson());        
    }
}