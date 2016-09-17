package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.exception.DatesMismatchException;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;

/**
 *
 * @author Stefan Remund
 * @since 27.08.2016
 * @version 1.0
 */
public class StampingPersistence extends GenericPersistence<Stamping, Long> {

    public static final String FIND_ALL_STAMPINGS_BY_DATE = "SELECT s FROM Stamping s WHERE s.stampingTime > :fromDate AND s.stampingTime < :tillDate AND s.assignedPerson.employee.id = :id";// s.assignedPerson

    /**
     * Persistiert einen Mandanten
     *
     * @param client
     */
    public void saveClient(Stamping stamping) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(stamping);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            logger.error("Stempelung existiert bereits!");
            entityManager.getTransaction().rollback();
        } catch (IllegalArgumentException e) {
            logger.error("Ungültige Stempelung!");
            entityManager.getTransaction().rollback();
        } catch (TransactionRequiredException e) {
            logger.error("Stempelung nicht vorhanden!");
            entityManager.getTransaction().rollback();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param p
     *            a Person object to retrieve unvalidated stampings from.
     * @return a list containing all unvalidated Stampings from the user passed via parameter.
     */
    public List<Stamping> getUnvalidatedStampings(Person p) {
        List<Stamping> unvalidatedStampings = new ArrayList<>();

        TypedQuery<Stamping> query = getEntityManager().createQuery("SELECT s FROM Stamping s "
                                                                    + "WHERE s.verificationStatus = 'Nicht Verifiziert' "
                                                                    + "AND s.assignedPerson.employee.id = :id",
                                                                    Stamping.class);

        query.setParameter("id", p.getId());

        try {
            unvalidatedStampings.addAll(query.getResultList());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return unvalidatedStampings;
    }

    /**
     * @param supervisor
     *            a person object to retrieve all verificationStatus from his subordinates
     * @return A list with VerificationStatus
     */
    public List<String> readAllVerificationStatus(Person supervisor) {
        if (supervisor == null) return null;

        TypedQuery<String> query = getEntityManager().createQuery("SELECT DISTINCT s.verificationStatus FROM Stamping s WHERE s.assignedPerson.employee.supervisor.id = :id",
                                                                  String.class);

        query.setParameter("id", supervisor.getId());

        try {
            List<String> allVerificationStatus = query.getResultList();
            return allVerificationStatus;
        } catch (NoResultException nre) {
            logger.error("No verification Status' found!");
            return new ArrayList<>();
        } catch (TransactionRequiredException tre) {
            logger.error("No open transaction found.");
            return null;
        } catch (QueryTimeoutException qtoe) {
            logger.error("SQL Timeout");
            return null;
        }
    }

    public void saveStamping(Stamping stamping) {
        StampingPersistence map = new StampingPersistence();
        DatabaseConnection.beginTransaction();
        map.makePersistent(stamping);
        DatabaseConnection.commitTransaction();
    }

    /**
     * @param s
     *            Stamping to update in the database.
     * @param loggedInPerson
     *            Person object to set the approvedBy-Attribute.
     */
    public void updateStamping(Stamping s, Person loggedInPerson) {
        logger.debug("Update stamping " + s.getId());
        EntityManager em = getEntityManager();

        try {
            Stamping dbStamping = em.find(Stamping.class, s.getId());

            em.getTransaction().begin();

            dbStamping.setVerificationStatus(s.getVerificationStatus());
            dbStamping.setVerifiedOn(Calendar.getInstance().getTime());
            dbStamping.setApprovedBy(loggedInPerson);
            dbStamping.setStampingTime(s.getStampingTime());
            dbStamping.setComment(s.getComment());

            em.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            logger.error("Illegal state, invalid stamping.");
            em.getTransaction().rollback();
        } catch (TransactionRequiredException e) {
            logger.error("No open transaction found!");
            em.getTransaction().rollback();
        } catch (Exception e) {
            logger.error(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    /**
     * @param person
     *            A Person object to retreive all Stampings during a period of time.
     * @param fromDate
     *            Marks the start of a certain period of time.
     * @param tillDate
     *            Marks the end of a certain period of time.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Stamping> getAllStampingsByMonth(Person person, Date fromDate, Date tillDate) {
        // Holt alle Stempelungen von einer Person

        Calendar ca = Calendar.getInstance();
        ca.setTime(tillDate);
        ca.add(Calendar.DATE, 1);

        Query query = getEntityManager().createQuery(FIND_ALL_STAMPINGS_BY_DATE);
        query.setParameter("fromDate", fromDate);
        query.setParameter("tillDate", ca.getTime());
        query.setParameter("id", person.getId());

        return query.getResultList();
    }

    /**
     * @param employmentPeriod
     *            EmploymentPeriod object to retreive all Stamping objects from.
     * @param startDate
     *            Marks the start of a certain period of time.
     * @param endDate
     *            Marks the end of a certain period of time.
     * @return A ordered List with Stampings during a certain period of time
     */
    public List<Stamping> getAllStampingsByDate(EmploymentPeriod employmentPeriod,
                                                Date date) throws DatesMismatchException {

        if ((employmentPeriod.getValidFrom() != null && employmentPeriod.getValidFrom().after(date))
            || (employmentPeriod.getValidTo() != null
                && employmentPeriod.getValidTo().before(date))) throw new DatesMismatchException();

        Calendar calStartDate = Calendar.getInstance();
        calStartDate.setTime(date);
        calStartDate.set(Calendar.SECOND, 0);
        calStartDate.set(Calendar.MINUTE, 0);
        calStartDate.set(Calendar.HOUR_OF_DAY, 0);

        Calendar calEndDate = Calendar.getInstance();
        calEndDate.setTime(calStartDate.getTime());
        calEndDate.add(Calendar.DAY_OF_YEAR, 1);

        TypedQuery<Stamping> stampingsByDate = getEntityManager().createNamedQuery(Stamping.FIND_ALL_STAMPINGS_BY_DATE,
                                                                                   Stamping.class);
        stampingsByDate.setParameter("startDate", calStartDate.getTime());
        stampingsByDate.setParameter("endDate", calEndDate.getTime());
        stampingsByDate.setParameter("period", employmentPeriod);

        return stampingsByDate.getResultList();
    }

    public List<Stamping> getStampingsPerPersonOrdered(EmploymentPeriod employmentPeriod, Date startDate,
                                                       Date endDate) {
        String sql = "SELECT s FROM Stamping s ";
        sql += "WHERE assignedperson=" + employmentPeriod.getId() + " ";
        sql += "AND stampingtime BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' ";
        sql += "ORDER BY stampingtime";
        TypedQuery<Stamping> q = getEntityManager().createQuery(sql, Stamping.class);
        List<Stamping> stampings = new ArrayList<Stamping>();
        try {
            stampings = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return stampings;
    }
}
