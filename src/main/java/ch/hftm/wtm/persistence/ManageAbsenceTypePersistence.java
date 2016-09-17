package ch.hftm.wtm.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.AbsenceType;

/**
 *
 * @author srdjankovacevic
 * @since 31.07.2016
 * @version 1.0
 */
public class ManageAbsenceTypePersistence extends GenericPersistence<AbsenceType, Long> {

    // TODO: Mach das schön!
    public List<AbsenceType> getAbsenceByClientId(Long client_id) {
        TypedQuery<AbsenceType> q = getEntityManager().createQuery("select at from AbsenceType at where at.client.id = :id",
                                                                   AbsenceType.class);
        q.setParameter("id", client_id);
        List<AbsenceType> absenceTypeList = null;
        try {
            absenceTypeList = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return absenceTypeList;
    }

    // TODO: Mach das schön!
    /**
     * Diese Methode prüft
     * 
     * @author srdjankovacevic
     * @since 13.09.2016
     * @param absenceType
     * @param client_id
     * @return
     */
    public boolean checkIfAbsenceTyepExisting(AbsenceType absenceType, Long client_id) {
        Query q = getEntityManager().createQuery("select at from AbsenceType at where lower(at.name) LIKE (:name) AND at.client.id =:id");
        q.setParameter("id", client_id);
        q.setParameter("name", absenceType.getName().toLowerCase());
        absenceType = null;
        try {
            absenceType = (AbsenceType) q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            // cry silently
        }
        return false;
    }

    public void saveAbsenceType(AbsenceType absenceType) {
        ManageAbsenceTypePersistence matp = new ManageAbsenceTypePersistence();
        DatabaseConnection.beginTransaction();
        matp.makePersistent(absenceType);
        DatabaseConnection.commitTransaction();
    }

}
