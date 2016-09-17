package ch.hftm.wtm.persistence;

import java.util.List;

import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.model.Client;

/**
 *
 * @author HOEFI
 * @since 04.09.2016
 * @version 1.0
 */
public class AbsenceTypePersistence  extends GenericPersistence<AbsenceType, Long> {

    public List<AbsenceType> getAbsenceTypesFromClient(Client cli) {
        String sql = "SELECT at FROM AbsenceType at WHERE client = " + cli.getId();
        return getEntityManager().createQuery(sql, AbsenceType.class).getResultList();
    }
}
