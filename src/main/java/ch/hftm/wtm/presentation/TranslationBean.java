package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "translationBean")
@SessionScoped
public class TranslationBean implements Serializable {
    private static final long serialVersionUID = 444861460936776097L;

    private HashMap<String, String> texts = new HashMap<String, String>();

    /**
     * @return the texts
     */
    public HashMap<String, String> getTexts() {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.texts",
                FacesContext.getCurrentInstance().getViewRoot().getLocale());
        Enumeration<String> keys = bundle.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = bundle.getString(key);
            texts.put(key, value.replaceAll("'", "\\\\'"));
        }

        return texts;
    }

    /**
     * @param texts the texts to set
     */
    public void setTexts(HashMap<String, String> texts) {
        this.texts = texts;
    }

    /**
     * Transformiere die texts map in ein Array weil JSF Maps nicht mag :<
     *
     * @return Object[]
     */
    public Object[] getJavascriptTexts() {
        return getTexts().entrySet().toArray();
    }

}
