package ch.hftm.wtm.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import ch.hftm.wtm.model.FiscalPeriod;

/**
 *
 * @author HOEFI
 * @since 08.08.2016
 * @version 1.0
 */
public class FiscalPeriodPersistence extends GenericPersistence<FiscalPeriod, Long> {

    @SuppressWarnings("unchecked")
    public List<FiscalPeriod> getFiscalPeriodsByClient(Long clientId) {
        String sql = "SELECT f FROM FiscalPeriod f WHERE f.client=:client";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("client", clientId);
        List<FiscalPeriod> periods = new ArrayList<FiscalPeriod>();
        try {
            periods = q.getResultList();
        } catch (NoResultException e) {
            // cry silently
        }
        return periods;
    }
}
