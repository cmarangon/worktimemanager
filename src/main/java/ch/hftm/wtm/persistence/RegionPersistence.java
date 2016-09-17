package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.Region;

public class RegionPersistence extends GenericPersistence<Region, Long> {
    public void saveRegion(Region r, Long client) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(r);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            logger.error("Region existiert bereits!");
            entityManager.getTransaction().rollback();
        } catch (IllegalArgumentException e) {
            logger.error("Ungültiger Region!");
            entityManager.getTransaction().rollback();
        } catch (TransactionRequiredException e) {
            logger.error("Transaktion nicht vorhanden!");
            entityManager.getTransaction().rollback();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * retrieve a Region object based on it's id
     * 
     * @param id id value to retrieve the matching Region object.
     * @return returns a match with the id passed via parameter.
     */
    public Region getRegion(Long id) {
        logger.debug("Get Region with id " + id.toString());

        TypedQuery<Region> query = getEntityManager().createQuery("SELECT r FROM Region r WHERE id = :id",
                Region.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            logger.error("No region found!");
            return new Region();
        } catch (TransactionRequiredException tre) {
            logger.error("No open transaction found.");
            return null;
        } catch (QueryTimeoutException qtoe) {
            logger.error("SQL Timeout");
            return null;
        }
    }

    /**
     * Gets all Region assigned to a certain client
     * 
     * @author stu157
     * @param c Client object to retrieve all regions from.
     * @return All regions from the Client object passed via parameter.
     */
    public List<Region> getAllRegionsByClient(Client c) {
        logger.debug("Get Regions from Client with Id " + c.getId());

        TypedQuery<Region> query = getEntityManager().createQuery(
                "SELECT r FROM Region r WHERE r.client.id = :id ORDER BY r.country ASC, r.name ASC", Region.class);
        query.setParameter("id", c.getId());

        try {
            return new ArrayList<Region>(query.getResultList());
        } catch (NoResultException nre) {
            logger.error("No Regions found!");
            return new ArrayList<>();
        } catch (TransactionRequiredException tre) {
            logger.error("No open transaction found.");
            return null;
        } catch (QueryTimeoutException qtoe) {
            logger.error("SQL Timeout");
            return null;
        }
    }

    /**
     * delete a certain Region object.
     * 
     * @param r Region object to delete.
     */
    public void deleteRegion(Region r) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(r);
            em.getTransaction().commit();
        } catch (IllegalStateException ise) {
            em.getTransaction().rollback();
            logger.error("Transaction could either not be opened or closed.");
        }
    }
}
