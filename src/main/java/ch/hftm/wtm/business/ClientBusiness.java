package ch.hftm.wtm.business;

import java.util.List;

import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.persistence.ClientPersistence;

/**
 * @author Claudio Marangon
 * @since 11.07.2016
 * @version 1.0
 */
public class ClientBusiness {

    // Persistence Klassen NIE als Klassenvariablen deklarieren sondern immer neu holen!
    // Grund: Somit können Probleme verhindert werden wenn zwischendurch auf die
    // Testdatenbank umgeschaltet wurde. So wird jeweils immer die korrekte DB-Connection gezogen.

    /**
     * @return Liste aller Mandanten
     */
    public List<Client> getClients() {
        return new ClientPersistence().findAll();
    }

    /**
     * @param id
     * @return Mandant anhand id
     */
    public Client getClientById(String id) {
        return getClientById(Integer.valueOf(id));
    }

    /**
     * @param id
     * @return Mandant anhand id
     */
    public Client getClientById(Integer id) {
        return new ClientPersistence().findById(Long.valueOf(id));
    }

    /**
     * Persistiert einen Mandanten
     *
     * @param client
     */
    public boolean saveClient(Client client) {
        return new ClientPersistence().save(client);
    }

    /**
     * Prüft ob ein Mandant mit dem Namen name existiert
     *
     * @param name
     * @return true/false
     */
    public boolean clientExists(String name) {
        if (new ClientPersistence().getClientByName(name) != null) {
            return true;
        }
        return false;
    }
}
