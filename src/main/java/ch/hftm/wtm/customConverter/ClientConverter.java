package ch.hftm.wtm.customConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ch.hftm.wtm.business.ClientBusiness;
import ch.hftm.wtm.model.Client;

@FacesConverter(value = "ch.hftm.wtm.customConverter.ClientConverter")
public class ClientConverter implements Converter {

    private ClientBusiness cb = new ClientBusiness();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return cb.getClientById(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String id = "";
        if (value instanceof Client) {
            Client client = (Client) value;
            id = String.valueOf(client.getId());
        }
        return id;
    }
}
