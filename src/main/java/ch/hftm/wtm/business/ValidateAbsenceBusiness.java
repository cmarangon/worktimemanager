package ch.hftm.wtm.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ch.hftm.wtm.model.Absence;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.AbsencePersistence;

public class ValidateAbsenceBusiness {

    protected Logger logger;
    
    /**
     * Submits all updated absences to the persistance layer.
     * @param allAbsences a List with all Absence objects.
     */
    public void submitAbsenceValdation(List<Absence> allAbsences) {
        AbsencePersistence ap = new AbsencePersistence();
        System.out.println("Updating " + allAbsences.size() + " Absences.");

        int errorCount = 0;

        try {
        for (Absence a : allAbsences) 
        {
            ap.getEntityManager().getTransaction().begin();
            ap.updateAbsence(a);
            ap.getEntityManager().getTransaction().commit();
        }
            
        }
        catch (Exception e) 
        {
            errorCount++;
            System.out.println(e.getMessage());
        }
        
        if(errorCount == 0)
            logger.info("Update Successful");
        else
            logger.error(errorCount + " Absences have not been updated.");
    }
    
    /**
     * reads all unvalidated absences from a certain person
     * @param p Person object to retreive all unvalidated absences from.
     * @return returns a list with all unvalidated absences from the person object passed via parameter.
     */
    public List<Absence> getUnvalidatedAbsences(Person p)
    {
        List<Absence> allUnvalidatedAbsences = new ArrayList<>();
        EmploymentPeriod ep = getActiveEmploymentperiod(p);
        
        if(ep == null)
            return new ArrayList<>();
        
        for(Absence a : ep.getAbsences())
            if(a.getIsVerified() == null)
                allUnvalidatedAbsences.add(a);
                
        return allUnvalidatedAbsences;
    }

    /**
     * Gets all Absences from the currently logged in user's subordinates.
     * @param loggedInPerson currently logged in person
     * @return a list with Absence objects from all users subordinated to the user passed via parameter.
     */
    public List<Absence> getAbsences(Person loggedInPerson)
    {
        if(loggedInPerson == null || loggedInPerson.getEmployees().size() == 0)
            return new ArrayList<>();
        
        List<Absence> allAbsences = new ArrayList<>();
        
        for(Person p : loggedInPerson.getEmployees())
        {
            if(p.isActive() == false)
                continue;
            
            allAbsences.addAll(getUnvalidatedAbsences(p));
        }
        
        return allAbsences;
    }
    
    /**
     * returns the active employment period for a user object.
     * @param p A person object to retreive the active EmploymentPeriod.
     * @return the only active employmentPeriod from the Person object passed via parameter.
     */
    private EmploymentPeriod getActiveEmploymentperiod(Person p)
    {
        for(EmploymentPeriod ep : p.getEmploymentPeriods())
        {
            if(ep.isActive())
                return ep;
        }
        return null;
    }
    
}
