package ch.hftm.wtm.customConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ch.hftm.wtm.model.Region;
import ch.hftm.wtm.persistence.RegionPersistence;

@FacesConverter(value = "ch.hftm.wtm.customConverter.RegionConverter")
public class RegionConverter implements Converter {

    private RegionPersistence regionPersister = new RegionPersistence();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return regionPersister.getRegion(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Region r = (Region)value;
        return r.getId().toString();
    }

}
