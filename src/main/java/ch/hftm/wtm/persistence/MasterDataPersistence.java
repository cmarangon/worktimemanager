package ch.hftm.wtm.persistence;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.MasterData;
import ch.hftm.wtm.model.Person;

public class MasterDataPersistence extends GenericPersistence<MasterData, Long> {

    public MasterData findActiveMasterData(Person person) {
        javax.persistence.Query q = getEntityManager()
                .createQuery("SELECT md FROM  MasterData md WHERE md.isActive = true AND md.client.id = :id");
        q.setParameter("id", person.getClient().getId());

        return (MasterData) q.getSingleResult();
    }

    /**
     * Retrieves an MasterData object from the database with the old values and detaches the submitted from it.
     * 
     * @author stefan
     * @param id The id of the eeded MasterData
     * @return An MasterData object from the database.
     */
    public MasterData getMasterData(MasterData md) {
        // DON'T you DARE to touch that thing!
        getEntityManager().detach(md);

        System.out.println("Submitted Id to Persistence class: " + md.getId());
        TypedQuery<MasterData> q = getEntityManager().createQuery("SELECT md FROM MasterData md WHERE md.id = :id",
                MasterData.class);
        q.setParameter("id", md.getId());

        MasterData dbMasterData;
        dbMasterData = q.getSingleResult();

        System.out.println("Id after detach: " + md.getId());
        return dbMasterData;
    }

    /**
     * Retrieves an MasterData object from the database
     * 
     * @author stefan
     * @param client Object to query from
     * @return An MasterData object from the database.
     */
    public MasterData getActiveMasterData(Client c) {
        TypedQuery<MasterData> q = getEntityManager().createQuery(
                "SELECT md FROM MasterData md WHERE md.client.id = :id AND isActive = true", MasterData.class);
        q.setParameter("id", c.getId());

        MasterData dbMasterData;

        try {
            dbMasterData = q.getSingleResult();
        } catch (NoResultException nre) {
            return new MasterData();
        } catch (NonUniqueResultException nure) {
            return (MasterData) q.getResultList().toArray()[0];
        }

        if (dbMasterData == null)
            return new MasterData();

        return dbMasterData;
    }

    /**
     * Retrieves an MasterData object from the database.
     * 
     * @author stefan
     * @param id The id of the needed MasterData
     * @return An MasterData object from the database.
     */
    public MasterData getMasterData(long id) {
        TypedQuery<MasterData> q = getEntityManager().createQuery("SELECT md FROM MasterData md WHERE md.id = :id",
                MasterData.class);
        q.setParameter("id", id);

        MasterData md = q.getSingleResult();
        return md;
    }

    public void persistMasterData(MasterData md) {
        getEntityManager().getTransaction().begin();
        makePersistent(md);
        getEntityManager().getTransaction().commit();
    }

    public void deactivateMasterData(MasterData md) {
        System.out.println("deactivate masterData " + md.getId());

        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        MasterData dbMasterData = getMasterData(md.getId());
        dbMasterData.setActive(false);
        dbMasterData.setValidToDate(Calendar.getInstance().getTime());
        em.getTransaction().commit();

        System.out.println("Done deactivating masterData " + md.getId());
    }
}
