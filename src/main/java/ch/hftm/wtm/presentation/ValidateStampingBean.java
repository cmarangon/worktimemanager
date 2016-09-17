
package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import ch.hftm.wtm.business.ValidateStampingBusiness;
import ch.hftm.wtm.model.Stamping;

@ManagedBean(name = "validateStampingBean")
@SessionScoped
public class ValidateStampingBean implements Serializable {
   
    public ValidateStampingBean() {
        System.out.println("validateStampingBean=" + Thread.currentThread().getId());

    }  
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean; 
    
    private List<String> allVerificationStatus;
    private static final long serialVersionUID = 12L;
    private List<Stamping> allStampings = new ArrayList<Stamping>();

    public void setAllVerificationStatus(List<String> allVerificationStatus)
    {
        this.allVerificationStatus = allVerificationStatus;
    }
    
    public List<String> getAllVerificationStatus()
    {
        return allVerificationStatus;
    }
    
    public List<Stamping> getAllStampings()
    {
        return allStampings;
    }
    
    public void setLoginBean(LoginBean loginBean)
    {
        this.loginBean = loginBean;
    }   
    
    private String noStampingsFound;

    public String getNoStampingsFound() {
        return noStampingsFound;
    }

    public void setNoStampingsFound(String noStampingsFound) {
        this.noStampingsFound = noStampingsFound;
    } 
    
    /**
     * called on load; initial work.
     * @return returns null.
     */
    public String onLoad() 
    {        
        readAllVerificationStatus();
        readAllStampings();
        
        return null;
    }
    
    /**
     * Submits the validation for all selected stampings.
     * @return returns null
     */
    public String submitValidation()
    {
        System.out.println("Submit Validations.");
        
        try
        {
            ValidateStampingBusiness vab = new ValidateStampingBusiness();
            vab.submitStampingValidation(allStampings, loginBean.getLoggedInPerson());
        }
        catch(Exception e)
        {
            
        }
        
        onLoad();
        return null;        
    }
    
    /**
     * Reads all VerificationStatus from all Stampings of all Subordinates of the currently logged in Person.
     */
    private void readAllVerificationStatus()
    {
        ValidateStampingBusiness vsb = new ValidateStampingBusiness();
        allVerificationStatus = vsb.getAllVerificationStatus(loginBean.getLoggedInPerson());
        System.out.println(allVerificationStatus.size());
    }
    
    /**
     * Reads all Stampings from all subordinates of the currently logged in person.
     */
    private void readAllStampings()
    {
        allStampings.clear();
        
        ValidateStampingBusiness vab = new ValidateStampingBusiness();
        vab.readStampings(loginBean.getLoggedInPerson());
        
        if(allStampings.size() == 0)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "texts");
            setNoStampingsFound(bundle.getString("no_stampings_to_validate"));
        }
        
        System.out.println(allStampings.size() + " stampings have been found.");
    }

}