package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

@ManagedBean(name = "templateBean")
@SessionScoped
public class TemplateBean implements Serializable {
    private static final long serialVersionUID = 5418377311815510551L;

    UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
    private boolean isCurrentView;

    /**
     * @return the isCurrentView
     */
    public boolean isCurrentView() {
        return isCurrentView;
    }

    /**
     * @param isCurrentView the isCurrentView to set
     */
    public void setCurrentView(boolean isCurrentView) {
        this.isCurrentView = isCurrentView;
    }

    // TODO: Das funktioniert nur mit einwenig magischem Glitterstaub. Schön machen!!
    public String printClasses(String page) {
        List<String> classList = new ArrayList<String>();

        switch (page) {
            case "overview":
                if (view.getViewId() == "/xhtml/pages/startSeite.xhtml") {
                    classList.add("active");
                }
                break;
            case "manageClient":

                if (view.getViewId().equals("/xhtml/pages/manageClient.xhtml")) {
                    classList.add("active");
                }
                break;
            default:

        }

        return String.join(" ", classList);
    }

}
