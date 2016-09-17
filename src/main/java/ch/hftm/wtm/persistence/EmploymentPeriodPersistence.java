package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;

/**
 *
 * @author srdjankovacevic
 * @since 24.08.2016
 * @version 1.0
 */
public class EmploymentPeriodPersistence extends GenericPersistence<EmploymentPeriod, Long> {

    /**
     * @param id
     *            Id from the person whose active EmploymentPeriod is requested.
     * @return Active EmploymentPeriod from the Person submitted via Parameter.
     */
    public EmploymentPeriod getActiveEmploymentPeriod(Long userId) {
        javax.persistence.Query q = getEntityManager()
                .createQuery("Select ep from  EmploymentPeriod ep where ep.employee.id=:id AND ep.isActive=:true");
        q.setParameter("id", userId);
        q.setParameter("true", true);
        EmploymentPeriod ep = null;
        try {
            ep = (EmploymentPeriod) q.getSingleResult();
            ep.getEmployee().getFirstName();
        } catch (NoResultException e) {
            // cry silently
        }
        return ep;
    }

    /**
     * Retrieves an EmploymentPeriod object from the database.
     *
     * @author stefan
     * @param id
     *            The id of theneeded EmploymentPeriod
     * @return An EmploymentPeriod obejct from the database.
     */
    public EmploymentPeriod getEmploymentPeriod(String employmentPeriodString) {
        TypedQuery<EmploymentPeriod> q = getEntityManager().createQuery("SELECT ep FROM  EmploymentPeriod ep",
                EmploymentPeriod.class);

        List<EmploymentPeriod> allPeriods = q.getResultList();
        System.out.println(allPeriods.size());

        for (EmploymentPeriod ep : allPeriods) {
            System.out.println(ep.toString());
            if (ep.toString().equals(employmentPeriodString))
                return ep;
        }

        return null;
    }

    /**
     * Retrieves an EmploymentPeriod object from the database with the old values and detaches the submitted from it.
     *
     * @author stefan
     * @param id
     *            The id of theneeded EmploymentPeriod
     * @return An EmploymentPeriod obejct from the database.
     */
    public EmploymentPeriod getEmploymentPeriod(EmploymentPeriod ep) {
        // DON'T you DARE to touch that thing!
        getEntityManager().detach(ep);

        System.out.println("Submitted Id to Persistence class: " + ep.getId());
        TypedQuery<EmploymentPeriod> q = getEntityManager().createQuery(
                "SELECT ep FROM EmploymentPeriod ep WHERE ep.id = :id",
                EmploymentPeriod.class);
        q.setParameter("id", ep.getId());

        EmploymentPeriod dbEmploymentPeriod;
        dbEmploymentPeriod = q.getSingleResult();
        System.out.println("detached: " + ep.getEmploymentLevelPercent());
        System.out.println("DB Entity: " + dbEmploymentPeriod.getEmploymentLevelPercent());
        System.out.println("Id after detach: " + ep.getId());
        return dbEmploymentPeriod;
    }

    /**
     * Retrieves an EmploymentPeriod object from the database.
     *
     * @author stefan
     * @param id
     *            The id of theneeded EmploymentPeriod
     * @return An EmploymentPeriod obejct from the database.
     */
    public EmploymentPeriod getEmploymentPeriod(long id) {
        TypedQuery<EmploymentPeriod> q = getEntityManager().createQuery(
                "SELECT ep FROM EmploymentPeriod ep WHERE ep.id = :id",
                EmploymentPeriod.class);
        q.setParameter("id", id);

        EmploymentPeriod ep = q.getSingleResult();
        // getEntityManager().detach(ep);
        return ep;
    }

    public void detachEntity(EmploymentPeriod ep) {
        try {
            getEntityManager().detach(ep);
            getEntityManager().clear();
        } catch (Exception e) {
            System.out.println("Employment Period could not be detached.");
        }
    }

    public List<EmploymentPeriod> getEmploymentPeriodsOrdered(Person person) {
        String sql = "SELECT ep FROM EmploymentPeriod ep ";
        sql += "WHERE employee=" + person.getId() + " ";
        sql += "ORDER BY validfrom ASC ";
        TypedQuery<EmploymentPeriod> q = getEntityManager().createQuery(sql, EmploymentPeriod.class);
        List<EmploymentPeriod> periods = new ArrayList<EmploymentPeriod>();
        try {
            periods = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return periods;
    }

    public List<EmploymentPeriod> findEmploymentPeriodByDatesAndPerson(Date fromDate, Date tillDate, Person person) {

        TypedQuery<EmploymentPeriod> q = getEntityManager().createQuery(
                "SELECT ep FROM EmploymentPeriod ep WHERE ep.employee.id=:id",
                EmploymentPeriod.class);
        q.setParameter("id", person.getId());

        EmploymentPeriod ep = null;
        try {
            ep = q.getSingleResult();
            System.out.println("das query gibt folgende Person zurück");
            ep.getEmployee().getFirstName();
        } catch (NoResultException e) {
            // cry silently
        }
        return q.getResultList();
    }

    public void persistEmploymentPeriod(EmploymentPeriod ep) {
        getEntityManager().getTransaction().begin();
        makePersistent(ep);
        getEntityManager().getTransaction().commit();
    }

    public void deactivateEmploymentPeriod(EmploymentPeriod ep) {
        System.out.println("deactivate ep " + ep.getId());

        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        EmploymentPeriod dbEmploymentPeriod = getEmploymentPeriod(ep.getId());
        dbEmploymentPeriod.setActive(false);
        dbEmploymentPeriod.setValidTo(Calendar.getInstance().getTime());
        em.getTransaction().commit();

        System.out.println("Done deactivating ep " + ep.getId());
    }
}
