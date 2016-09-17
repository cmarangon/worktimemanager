
package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import ch.hftm.wtm.business.ValidateAbsenceBusiness;
import ch.hftm.wtm.model.Absence;

@ManagedBean(name = "validateAbsenceBean")
@SessionScoped
public class ValidateAbsenceBean implements Serializable {
    
    protected Logger logger;
    private static final long serialVersionUID = 8L;
    private List<Absence> allAbsences = new ArrayList<Absence>();
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean; 

    public List<Absence> getAllAbsences()
    {
        return allAbsences;
    }
    
    public void setLoginBean(LoginBean loginBean)
    {
        this.loginBean = loginBean;
    }   
    
    private String noAbsencesFound;

    public String getNoAbsencesFound() {
        return noAbsencesFound;
    }

    public void setNoAbsencesFound(String noAbsencesFound) {
        this.noAbsencesFound = noAbsencesFound;
    } 
    
    /**
     * called on load, doing some initial work.
     * @return returns null.
     */
    public String onLoad() 
    {        
        readAllAbsences();
        
        return null;
    }
    
    /**
     * Submits the Validation for all unvalidated absences.
     * @return returns null.
     */
    public String submitValidation()
    {
        try
        {
            ValidateAbsenceBusiness vab = new ValidateAbsenceBusiness();
            vab.submitAbsenceValdation(allAbsences);
        }
        catch(Exception e)
        {
            MessageFactory.error("ch.hftm.wtm.VALIDATE_ABSENCE_UNEXPECTED_ERROR");
        }
        
        onLoad();
        return null;        
    }
    
    /**
     * reads all unvalidatedAbsences from the currently logged in user's subordinates.
     */
    public void readAllAbsences()
    {
        allAbsences.clear();
        
        ValidateAbsenceBusiness vab = new ValidateAbsenceBusiness();
        allAbsences.addAll(vab.getAbsences(loginBean.getLoggedInPerson()));
        
        if(allAbsences.size() == 0)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "texts");
            setNoAbsencesFound(bundle.getString("no_absences_to_validate"));
        }
        
        //logger.debug(allAbsences.size() + " Absences have been found.");
    }

}