package ch.hftm.wtm.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ch.hftm.wtm.business.ClientBusiness;
import ch.hftm.wtm.business.LoginBusiness;
import ch.hftm.wtm.business.PersonBusiness;
import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.exception.LoginPasswordNotCorrectException;
import ch.hftm.wtm.exception.LoginUserHasInactiveClient;
import ch.hftm.wtm.exception.LoginUserNotFoundException;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.ClientPersistence;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 2039076461378512891L;

    // Eingabefelder
    private String userId;
    private String password;

    // Klassenvariablen
    private Person loggedInPerson;
    private boolean isLocked;
    private String pattern = "[a-z\\.+]+\\@[a-z]+\\.[a-z]{2,4}";
    private String lastPage = "";
    private Client pretendedClient = null;

    public LoginBean() {
        PersonBusiness personBusiness = new PersonBusiness();
        personBusiness.createSystemAdministrator();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId.matches(pattern)) {
            this.userId = userId;
        } else {
            MessageFactory.info("ch.hftm.wtm.login.INVALID_USER_ID_FORMAT");
            this.userId = userId;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the pretendedClient
     */
    public Client getPretendedClient() {
        if (pretendedClient == null) {
            pretendedClient = loggedInPerson.getClient();
        }

        return pretendedClient;
    }

    /**
     * @param pretendedClient the pretendedClient to set
     */
    public void setPretendedClient(Client pretendedClient) {
        this.pretendedClient = pretendedClient;
    }

    /**
     * @return the clients
     */
    public List<Client> getClients() {
        ClientBusiness cb = new ClientBusiness();
        return cb.getClients();
    }

    public Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public boolean isSuperior() {
        if (isAdmin())
            return true;

        if (loggedInPerson != null)
            if (loggedInPerson.getEmployees().size() > 0)
                return true;

        return false;
    }

    public boolean isAdmin() {
        if (isSysAdmin())
            return true;

        if (loggedInPerson != null)
            if (loggedInPerson.getAssignedUserRole().equals(UserRole.ADMIN))
                return true;

        return false;
    }

    public boolean isSysAdmin() {
        if (loggedInPerson != null)
            if (loggedInPerson.getAssignedUserRole().equals(UserRole.SYSADMIN))
                return true;

        return false;
    }

    public Client getClientFromLoggedInPerson() {
        if (loggedInPerson == null)
            return null;
        if (isSysAdmin() && pretendedClient != null) {
            ClientPersistence cp = new ClientPersistence();
            if (cp.findById(pretendedClient.getId()) != null)
                return pretendedClient;
        }

        return loggedInPerson.getClient();
    }

    public boolean isLoggedIn() {
        return loggedInPerson != null;
    }

    public String onLoad() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String viewId = facesContext.getViewRoot().getViewId();

        try {
            if (viewId.equals("/xhtml/pages/login.xhtml")) {
                if (isLoggedIn())
                    externalContext.redirect("startSeite.xhtml");
            } else if (!isLoggedIn()) {
                if (viewId != null)
                    lastPage = viewId;
                externalContext.redirect("login.xhtml");
            } else if (isLocked) {
                externalContext.redirect("lock.xhtml");
            }
        } catch (IOException e) {
            // cry silently
        }
        return null;
    }

    public String clear() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.userId = null;
        this.password = null;
        this.loggedInPerson = null;

        isLocked = false;
        return null;
    }

    public String verifyLogin() {
        LoginBusiness lb = new LoginBusiness();
        try {
            loggedInPerson = lb.verifyLogin(userId, password);
            // Wenn Login erfolgreich, gehe zur Startseite
            if (loggedInPerson != null) {
                System.out.println("Person mit folgendem Namen wurde eingelogt: " + loggedInPerson.getFirstName() + " "
                        + loggedInPerson.getLastName());
                if (!lastPage.isEmpty())
                    return lastPage;
                return "startSeite";
            } else {
                // Wenn login nicht erfolgreich, bleibe auf der Seite
                return null;
            }
        } catch (LoginPasswordNotCorrectException e) {
            MessageFactory.error("ch.hftm.wtm.login.PASSWORD_NOT_CORRECT");
        } catch (LoginUserNotFoundException e) {
            MessageFactory.error("ch.hftm.wtm.login.USER_NOT_FOUND");
        } catch (LoginUserHasInactiveClient e) {
            MessageFactory.error("ch.hftm.wtm.login.CLIENT_INACTIVE");
        }

        return null;
    }

    public String unlockUser() {
        LoginBusiness lb = new LoginBusiness();
        try {
            // Wenn Login erfolgreich, gehe zur Startseite
            if (lb.verifyPassword(loggedInPerson, password)) {
                System.out.println("Person mit folgendem Namen hat sich entsperrt: " + loggedInPerson.getFirstName()
                        + " " + loggedInPerson.getLastName());
                isLocked = false;
                return "startSeite";
            } else {
                // Wenn login nicht erfolgreich, bleibe auf der Seite
                return null;
            }
        } catch (LoginPasswordNotCorrectException e) {
            MessageFactory.error("ch.hftm.login.PASSWORD_NOT_CORRECT");
            return null;
        }
    }

    public String lockUser() {
        System.out.println("Person mit folgendem Namen hat sich gesperrt: " + loggedInPerson.getFirstName() + " "
                + loggedInPerson.getLastName());
        isLocked = true;
        return "lock.xhtml";
    }

    public String logOut() {
        clear();
        return "login.xhtml";
    }
}