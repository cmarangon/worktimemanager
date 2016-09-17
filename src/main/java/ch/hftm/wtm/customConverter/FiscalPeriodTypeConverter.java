package ch.hftm.wtm.customConverter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ch.hftm.wtm.model.FiscalPeriod;
import ch.hftm.wtm.persistence.FiscalPeriodPersistence;

/**
 *
 * @author HOEFI
 * @since 04.08.2016
 * @version 1.0
 */
@ManagedBean(name = "fiscalPeriodTypeConverter")
public class FiscalPeriodTypeConverter implements Converter {

    private FiscalPeriodPersistence fiscalPersist = new FiscalPeriodPersistence();

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("FiscalPeriod: " + value);
        return fiscalPersist.findById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((FiscalPeriod) value).getId().toString();
    }

}
