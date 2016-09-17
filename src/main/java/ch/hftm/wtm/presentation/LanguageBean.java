package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * @author Kove
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {

    private static final long serialVersionUID = 1139760232263527160L;
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public String english() {
        setLocale(Locale.ENGLISH);
        return null;
    }

    public String german() {
        setLocale(Locale.GERMAN);
        return null;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

    public Locale getLocale() {
        return locale;
    }

}
