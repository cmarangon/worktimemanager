package ch.hftm.wtm.customConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.PersonPersistence;

@FacesConverter(value = "ch.hftm.wtm.customConverter.PersonConverter")
public class PersonConverter implements Converter {

    //private ReportBusiness reportBusiness = ReportBusiness.getInstance();
    private PersonPersistence pp = new PersonPersistence();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return pp.getPerson(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String s = "";
        if (value instanceof Person) {
            Person person = (Person) value;
            s += person.getEmail();
            return s;
        }
        return s;
    }

}
