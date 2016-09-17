package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ch.hftm.wtm.business.ClientBusiness;
import ch.hftm.wtm.business.PersonBusiness;
import ch.hftm.wtm.enumeration.UserRole;
import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Person;

@ManagedBean(name = "manageClientBean")
@RequestScoped
public class ManageClientBean implements Serializable {
    private static final long serialVersionUID = -3271448299155445895L;

    private Client client = new Client();
    private Person administrator = new Person();

    private List<Client> clients = new ClientBusiness().getClients();

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return the administrator
     */
    public Person getAdministrator() {
        return administrator;
    }

    /**
     * @param administrator the administrator to set
     */
    public void setAdministrator(Person administrator) {
        this.administrator = administrator;
    }

    /**
     * @return the client
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * @param client the client to set
     */
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public String save() {
        // Starte Transaction
        ClientBusiness cb = new ClientBusiness();
        if (!cb.clientExists(client.getCompanyName())) {
            PersonBusiness pb = new PersonBusiness();
            if (!pb.personExists(administrator.getEmail())) {

                administrator.setAssignedUserRole(UserRole.ADMIN);
                administrator.setPassword("notsorandomhash");

                pb.savePerson(administrator);
            }
            client.setClientAdmin(administrator);
            cb.saveClient(client);

            // save client before adding to admin
            administrator.setClient(client);
            pb.savePerson(administrator);

            client = new Client();
            administrator = new Person();
        }

        return "overviewClient";
    }

    public String checkCompanynameAvailability() throws Exception {
        if (new ClientBusiness().clientExists(client.getCompanyName())) {
            throw new Exception("meaninfulerrormessage");
        }
        return "";
    }

    public void setActiveStatus() {
        ClientBusiness cb = new ClientBusiness();
        for (Client client : clients) {
            cb.saveClient(client);
        }
    }
}
