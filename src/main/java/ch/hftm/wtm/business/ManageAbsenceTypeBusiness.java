package ch.hftm.wtm.business;

import java.util.List;

import ch.hftm.wtm.model.AbsenceType;
import ch.hftm.wtm.persistence.ManageAbsenceTypePersistence;

/**
 * @author srdjankovacevic
 * @since 31.07.2016
 * @version 1.0
 */
public class ManageAbsenceTypeBusiness {

    private ManageAbsenceTypePersistence manageAbsenceTypePersistence = new ManageAbsenceTypePersistence();

    /**
     * Hole die AbsenzTypen aus der der Datenbank, welche dem entsprechenden Clienten zugewiesen sind
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param client_id
     * @return
     */
    public List<AbsenceType> getAbsenceTypeByClientId(Long client_id) {
        List<AbsenceType> absensceTypeList = manageAbsenceTypePersistence.getAbsenceByClientId(client_id);
        return absensceTypeList;
    }

    /**
     * Speichere die Absenztypen in die Datenbank
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absenceType
     */
    public void saveAbsenceType(AbsenceType absenceType) {
        manageAbsenceTypePersistence.saveAbsenceType(absenceType);
    }

    public boolean checkIfAbsencetypeExisting(AbsenceType absenceType, Long client_id) {

        return manageAbsenceTypePersistence.checkIfAbsenceTyepExisting(absenceType, client_id);
    }

    /**
     * Lösche den übergeben Absenztyp aus der Datenbank
     * 
     * @author srdjankovacevic
     * @since 04.09.2016
     * @param absenceType
     */
    public void deleteAbsenceType(AbsenceType absenceType) {
        manageAbsenceTypePersistence.makeTransient(absenceType);
    }
}