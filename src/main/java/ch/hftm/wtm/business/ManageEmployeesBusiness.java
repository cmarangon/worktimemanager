package ch.hftm.wtm.business;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ch.hftm.wtm.exception.AttributeUpdateFailedException;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.persistence.PersonPersistence;

public class ManageEmployeesBusiness {
    
    /**
     * Submits a new Person object to the persistence layer.
     * @param p new Person object to persist.
     */
    public void submitNewEmployee(Person p)
    {
        PersonPersistence pp = new PersonPersistence();
        pp.getEntityManager().getTransaction().begin();
        
        pp.makePersistent(p);
        
        pp.getEntityManager().getTransaction().commit();
    }
    
    /**
     * @param p Object to get all Employees from a client
     * @return a list of Persons from the client of the person passed via parameter.
     */
    public Set<Person> getAllEmployees(Person p)
    {
        Set<Person> allEmployees = new HashSet<>();
        
        PersonPersistence pp = new PersonPersistence();
        p = pp.getPerson(p.getEmail());
        
        for(Person singleEmployee : p.getClient().getEmployees())
            if(singleEmployee.isActive())
                allEmployees.add(singleEmployee);
        
        return allEmployees;
    }
   
    /**
     * Submits a existing Person object to the Persistence layer.
     * @param p Submit all changes made on a Person object.
     * @throws AttributeUpdateFailedException 
     */
    public void submitEmployeeChanges(Person p) throws AttributeUpdateFailedException
    {
        System.out.println("SubmitChanges on Employee");
        PersonPersistence pp = new PersonPersistence();
        pp.getEntityManager().detach(p);        
        
        Person dbPerson = pp.getEntityManager().find(Person.class, p.getId());
        pp.getEntityManager().getTransaction().begin();
        
        if(dbPerson != null)
            System.out.println(dbPerson.getBusinessPhoneNumber() + " vs old number " + p.getBusinessPhoneNumber());
        
        try
        {
            dbPerson.setFirstName(p.getFirstName());
            dbPerson.setLastName(p.getLastName());
            dbPerson.setEmail(p.getEmail());
            dbPerson.setPassword(p.getPassword());
            dbPerson.setStreet(p.getStreet());
            dbPerson.setPostalCode(p.getPostalCode());
            dbPerson.setCity(p.getCity());
            dbPerson.setPrivatePhoneNumber(p.getPrivatePhoneNumber());
            dbPerson.setBusinessPhoneNumber(p.getBusinessPhoneNumber());
            dbPerson.setSupervisor(p.getSupervisor());
            dbPerson.setClient(p.getClient());
        }
        catch(Exception e){ throw new AttributeUpdateFailedException();}
        
        
        if(p.isActive() == false && dbPerson.isActive() == true)
        {
            System.out.println("Deactivate User");
            dbPerson.setActive(false);
            dbPerson.setLeavingDate((Date)Calendar.getInstance().getTime());
        }
        
        pp.getEntityManager().getTransaction().commit();
    }
    
    /**
     * deletes an Employee object from the database
     * @param p Person object to delete;
     */
    public void deactivateEmployee(Person p)
    {
        PersonPersistence pp = new PersonPersistence();
        pp.deactivatePerson(p);
        
        return;
    }
}
