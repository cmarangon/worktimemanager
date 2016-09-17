package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.Person;

/**
 *
 * @author srdjankovacevic
 * @since 31.07.2016
 * @version 1.0
 */
public class ManageAbsencePersistence extends GenericPersistence<Absence, Long> {

    // TODO: Mach das schön!
    public List<Absence> findAbsenceListById(Long person_id) {
        TypedQuery<Absence> q = getEntityManager().createQuery("select a from Absence a where a.person.employee.id = :id",
                                                               Absence.class);
        q.setParameter("id", person_id);

        List<Absence> absenceList = null;
        try {
            absenceList = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return absenceList;
    }

    // TODO: Mach das schön!
    public Absence findAbsenceById(Long absence_id) {
        javax.persistence.Query q = getEntityManager().createQuery("select a from Absence a where id = :id");
        q.setParameter("id", absence_id);

        Absence absence = null;
        try {
            absence = (Absence) q.getSingleResult();
        } catch (NoResultException e) {
            // cry silently
        }
        return absence;
    }

    public void removeExistingAbsence(Long absence_id) {
        javax.persistence.Query q = getEntityManager().createQuery("delete FROM Absence where id = :id");
        q.setParameter("id", absence_id);
        getEntityManager().joinTransaction();
        q.executeUpdate();
    }

    public List<Absence> getAbsencePerPersonOrdered(Person person, Date startDate, Date endDate) {
        String sql = "SELECT a FROM Absence a ";
        sql += "WHERE person=" + person.getId() + " ";
        sql += "AND start BETWEEN '" + startDate + "' AND '" + endDate + "' ";
        sql += "ORDER BY start";
        TypedQuery<Absence> q = getEntityManager().createQuery(sql, Absence.class);
        List<Absence> absences = new ArrayList<Absence>();
        try {
            absences = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return absences;
    }

    public void saveAbsence(Absence absence) {

        ManageAbsencePersistence map = new ManageAbsencePersistence();
        DatabaseConnection.beginTransaction();
        map.makePersistent(absence);
        DatabaseConnection.commitTransaction();

    }

}