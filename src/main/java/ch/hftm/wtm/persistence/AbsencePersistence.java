package ch.hftm.wtm.persistence;



import java.util.Calendar;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

import ch.hftm.wtm.model.*;

/**
 *
 * @author Stefan Remund
 * @since 27.08.2016
 * @version 1.0
 */
public class AbsencePersistence extends GenericPersistence<Absence, Long> {


    /**
     * Persistiert eine Absenz
     *
     * @param absence
     */
    public void saveClient(Absence absence) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(absence);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            logger.error("Absenz existiert bereits!");
            entityManager.getTransaction().rollback();
        } catch (IllegalArgumentException e) {
            logger.error("Ungültige Absenz!");
            entityManager.getTransaction().rollback();
        } catch (TransactionRequiredException e) {
            logger.error("Absenz nicht vorhanden!");
            entityManager.getTransaction().rollback();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void updateAbsence(Absence a)
    {
        System.out.println("Update Absence " + a.getId());
        EntityManager entityManager = getEntityManager();
        Absence dbAbsence = entityManager.find(Absence.class, a.getId());
        
        System.out.println(dbAbsence.getIsVerified().toString() + dbAbsence.getIsVerified().toString());
        dbAbsence.setIsVerified(a.getIsVerified());
        dbAbsence.setVerifiedOn(Calendar.getInstance().getTime());
    }
    /*
     * Query query = entityManager
                        .createQuery("UPDATE Absence absence SET Comment = 'Updated'" //:isVerified "
                        + "WHERE absence.id = :id");
                        //query.setParameter("isVerified", a.getIsVerified());
                        query.setParameter("id", a.getId());
        query.executeUpdate();
        */
}
