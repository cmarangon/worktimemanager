package ch.hftm.wtm.persistence;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Client;

/**
 *
 * @author Claudio Marangon
 * @since 11.07.2016
 * @version 1.0
 */
public class ClientPersistence extends GenericPersistence<Client, Long> {

    /**
     * Sucht nach einem bestimmten Mandant
     *
     * @param name
     * @return Der Mandant oder null bei keiner Übereinstimmung
     */
    public Client getClientByName(String name) {
        Client c = null;
        TypedQuery<Client> query = getEntityManager().createQuery(
                "SELECT c FROM Client c WHERE c.companyName = :name", Client.class);

        try {
            c = query.setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            // cry silently
        }

        return c;
    }

    public boolean save(Client client) {
        try {
            DatabaseConnection.beginTransaction();
            makePersistent(client);
            DatabaseConnection.commitTransaction();
        } catch (EntityExistsException e) {
            DatabaseConnection.rollbackTransaction();
            return false;
        } catch (IllegalArgumentException e) {
            DatabaseConnection.rollbackTransaction();
            return false;
        } catch (TransactionRequiredException e) {
            DatabaseConnection.rollbackTransaction();
            return false;
        } catch (Exception e) {
            DatabaseConnection.rollbackTransaction();
            return false;
        }

        return true;
    }
}
