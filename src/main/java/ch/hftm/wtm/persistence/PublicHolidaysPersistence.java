package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Client;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.PublicHoliday;

public class PublicHolidaysPersistence extends GenericPersistence<PublicHoliday, Long> {

    public void savePublicHoliday(PublicHoliday ph) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(ph);
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
    
    public PublicHoliday getPublicHoliday(Long id)
    { 
        return getEntityManager().find(PublicHoliday.class, id);
    }
    
    /**
     * writes all changes on a PublicHoliday object into the database.
     * @param ph PublicHoliday object to update into the database.
     */
    public void updatePublicHoliday(PublicHoliday ph) {
        EntityManager em = getEntityManager();
        
        try {
            em.getTransaction().begin();
        
            PublicHoliday dbPublicHoliday = getEntityManager().find(PublicHoliday.class, ph.getId());
            
            dbPublicHoliday.getAppliedRegions().clear();
            dbPublicHoliday.getAppliedRegions().addAll(ph.getAppliedRegions());
            System.out.println("DB Instance: " + dbPublicHoliday.getAppliedRegions().size());

            dbPublicHoliday.setClient(getEntityManager().find(Client.class, ph.getClient().getId()));
            dbPublicHoliday.setDayOfTheYear(ph.getDayOfTheYear());
            dbPublicHoliday.setDuration(ph.getDuration());
            dbPublicHoliday.setIsActive(ph.getIsActive());
            dbPublicHoliday.setTitle(ph.getTitle());

            getEntityManager().getTransaction().commit();
            
            ph = getEntityManager().find(PublicHoliday.class, ph.getId());
            logger.info("PublicHoliday object successfuly updated.");
        }
        catch (IllegalArgumentException e) 
        {
            logger.error("Invalid attributes in PublicHoliday!");
            
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        catch (TransactionRequiredException e) 
        {
            logger.error("No open transaction found.");
            
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
        catch (Exception e) 
        {
            logger.error(e.getMessage());
        }
    }

    /**
     * @param c client object to retrieve all assigned PublicHolidays from
     * @return a whole set of PublicHolidays assigned to the client passed via parameter
     */
    public List<PublicHoliday> getAllPublicHolidays(Client c) {
        logger.debug("Get PublicHolidays from Client with id " + c.getId());
        
        TypedQuery<PublicHoliday> query = getEntityManager().createQuery("SELECT ph FROM PublicHoliday ph WHERE ph.client.id = :id ORDER BY ph.title ASC", PublicHoliday.class);
        query.setParameter("id", c.getId());

        try
        {
            List<PublicHoliday> publicHolidays = query.getResultList();
            return publicHolidays;
        }
        catch(NoResultException nre) 
        { 
            logger.error("No PublicHolidays found!");
            return new ArrayList<>(); 
        }
        catch(TransactionRequiredException tre)
        { 
            logger.error("No open transaction found.");
            return null; 
        }
        catch(QueryTimeoutException qtoe) 
        {
            logger.error("SQL Timeout");
            return null; 
        }
    }
}