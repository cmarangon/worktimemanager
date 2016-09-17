package ch.hftm.wtm.persistence;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import ch.hftm.wtm.model.Person;

public class PersonPersistence extends GenericPersistence<Person, Long> {

    // TODO: Mach das schön!
    public Person findPersonByEmail(String userId) {
        userId.toLowerCase();
        String sql = "SELECT p FROM Person p WHERE lower(p.email) LIKE lower(:email)";
        Query q = getEntityManager().createQuery(sql);
        q.setParameter("email", userId);
        Person p = null;
        try {
            p = (Person) q.getSingleResult();
        } catch (NoResultException e) {
            // cry silently
        }
        return p;
    }
    
    /**
     * queries a user based on his email address.
     * @param email emailaddress to get the matching user object.
     * @return returns a Perosn object based on the email passed via parameter.
     */
    public Person getPerson(String email)
    {
        EntityManager em = getEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class);
        query.setParameter("email", email);
        
        try
        { 
            Person p = query.getSingleResult(); 
            return p; 
        }
        catch(NoResultException nre) 
        { 
            logger.error("No person found!");
            return new Person(); 
        }
        catch(TransactionRequiredException tre)
        { 
            logger.error("No open transaction found.");
            return null; 
        }
        catch(QueryTimeoutException qtoe) 
        {
            logger.error("SQL Timeout");
            return null; 
        }
    }
    
    /**
     * Deactivates a Person object
     * @param p Person object to be deactivated.
     */
    public void deactivatePerson(Person p)
    {
        EntityManager em = getEntityManager();
        
        try
        {
            em.getTransaction().begin();
            
            Person dbPerson = em.find(Person.class, p.getId());
            dbPerson.setActive(false);
            dbPerson.setLeavingDate(Calendar.getInstance().getTime());
            
            em.getTransaction().commit();
        }
        catch(IllegalStateException ise)
        {
            em.getTransaction().rollback();
            logger.error("Transaction could either not be opened or closed.");
        }
    }
}