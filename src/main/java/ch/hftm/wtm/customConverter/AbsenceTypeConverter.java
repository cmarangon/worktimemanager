package ch.hftm.wtm.customConverter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.persistence.ManageAbsenceTypePersistence;

/**
 *
 * @author srdjankovacevic
 * @since 04.08.2016
 * @version 1.0
 */
@ManagedBean(name = "userConverterBean")

public class AbsenceTypeConverter implements Converter {

    @PersistenceContext
    EntityManager entityManager;

    private ManageAbsenceTypePersistence matp = new ManageAbsenceTypePersistence();

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */

    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        return (value == null) ? null : matp.findById(Long.parseLong(value));

    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {

        return (value == null) ? null : Long.toString(((AbsenceType) value).getId());

    }

}
