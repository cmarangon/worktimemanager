
package ch.hftm.wtm.presentation;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import ch.hftm.wtm.business.ManageMetaDataBusiness;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.MasterData;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.MasterDataPersistence;

@ManagedBean(name = "manageMetaDataBean")
@SessionScoped
public class ManageMetaDataBean implements Serializable {

    private static final long serialVersionUID = 123872L;
    protected Logger logger = Logger.getLogger(getClass());

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private Client currentClient;
    private MasterData currentMasterData;
    private boolean isNewMasterData = false;

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public MasterData getCurrentMasterData() {
        return currentMasterData;
    }

    public void setCurrentMasterData(MasterData currentMasterData) {
        this.currentMasterData = currentMasterData;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    /**
     * Called on load, doing some initialization work
     * 
     * @author stu157
     * @return return Null
     */
    public String onLoad() {
        System.out.println("On Load manageMetaDataBean");

        try {
            Person p = loginBean.getLoggedInPerson();

            System.out.println(p);
            System.out.println(p.getEmploymentPeriods().size());

            setCurrentClient(p.getClient());
        } catch (NullPointerException npe) {
            System.out.println("Person is null");
            return null;
        }

        MasterDataPersistence mdp = new MasterDataPersistence();
        if (currentClient != null) {
            currentMasterData = mdp.getActiveMasterData(currentClient);

            if (currentMasterData.getId() == null) {
                isNewMasterData = true;
                currentMasterData.setClient(currentClient);
            }
        } else
            System.out.println("Current Client = null");

        return null;
    }

    /**
     * @author stu157
     *         Submits the changes from the form to the database.
     */
    public void submit() {
        System.out.println("Submit selected metaData: " + currentMasterData.getId());
        ManageMetaDataBusiness mmdb = new ManageMetaDataBusiness();

        mmdb.submitChanges(currentMasterData, isNewMasterData);

        isNewMasterData = false;
    }
}