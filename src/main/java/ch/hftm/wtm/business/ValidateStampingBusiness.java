package ch.hftm.wtm.business;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.StampingPersistence;

/**
 * @author stu157
 */
public class ValidateStampingBusiness {
    StampingPersistence sp = new StampingPersistence();
    
    
    /**
     * @param allStampings a List with Stampings for a certain users subordinates.
     * @param loggedInPerson the currently logged in person to set the verifiedBy-Attribute
     */
    public void submitStampingValidation(List<Stamping> allStampings, Person loggedInPerson) {
        System.out.println("Updating " + allStampings.size() + " stampings.");

        int errorCount = 0;
        for (Stamping s : allStampings) 
        {
            try { sp.updateStamping(s, loggedInPerson); }
            catch (Exception e) 
            {
                errorCount++;
                throw e;
            }
        }
        
        if(errorCount == 0)
            System.out.println("Update Successful");
        else
            System.out.println(errorCount + " stampings have not been updated.");
    }
    
    /**
     * @param loggedInPerson logged in Person from the LoginBean
     * @return returns a list with all unvalidated Stamping from the employees of the currently logged in user.
     */
    public List<Stamping> readStampings(Person loggedInPerson)
    {
        List<Stamping> allStampings = new ArrayList<>();
        
        if(loggedInPerson == null || loggedInPerson.getEmployees().size() == 0)
            return new ArrayList<>();;
            
        for(Person p : loggedInPerson.getEmployees())
            allStampings.addAll(getUnvalidatedStampings(p));
        
        return allStampings;
    }
    
    
    /**
     * @param p Person Object to retrieve unvalidated Stampings from.
     * @return Returns a list with Stampings from the user passed via parameter.
     */
    public List<Stamping> getUnvalidatedStampings(Person p)
    {
        List<Stamping> allUnvalidatedStampings = new ArrayList<>();
        
        allUnvalidatedStampings.addAll(sp.getUnvalidatedStampings(p));
                
        return allUnvalidatedStampings;
    }

    /**
     * @param p User to get all distinct verificationStatus from the database 
     * @return returns a list with all verificationstatus retreived from the database.
     */
    public List<String> getAllVerificationStatus(Person p)
    {
        return sp.readAllVerificationStatus(p);
    }    
}
