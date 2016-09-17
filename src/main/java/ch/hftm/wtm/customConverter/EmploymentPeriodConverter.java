package ch.hftm.wtm.customConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.persistence.EmploymentPeriodPersistence;

@FacesConverter(value = "ch.hftm.wtm.customConverter.EmploymentPeriodConverter")
public class EmploymentPeriodConverter implements Converter {

    private EmploymentPeriodPersistence employmentPeriodPersister = new EmploymentPeriodPersistence();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("getAsObject");
        EmploymentPeriod ep = employmentPeriodPersister.getEmploymentPeriod(Long.valueOf(value));
        
        if(ep == null)
            ep = new EmploymentPeriod();
        
        System.out.println("EmploymentPeriod Id from DB " + ep.getId());
        return ep;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        System.out.println("getAsString");
        try
        {
            EmploymentPeriod ep = (EmploymentPeriod)value;
            System.out.println("GetAsString Id: " + ep.getId());
            return ep.getId().toString();
        }
        catch(Exception e)
        {
            System.out.println("Exception: " + value);
            return "";
        }
    }

}
