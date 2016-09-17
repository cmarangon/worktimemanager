package ch.hftm.wtm.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 *
 * Abstract class to use and manage a database with JPA
 *
 * @date 20.10.2014
 *
 * @author michael.mueller
 *
 * @param <T>
 *            Generic Type of a persistence class
 * @param <ID>
 *            Long
 */

public abstract class GenericPersistence<T, ID extends Serializable> {

    // protected EntityManager entityManager;

    private Class<T> persistenceClass;
    protected Logger logger;

    /**
     * Instance of entity manager factory
     */
    @SuppressWarnings("unchecked")
    public GenericPersistence() {
        // entityManager = DatabaseConnection.currentEntityManager();
        persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        logger = Logger.getLogger(getClass());
    }

    /**
     * Find all of persistence class
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        logger.debug("Find all, called by: " + persistenceClass.getSimpleName());
        return getEntityManager().createQuery("from " + persistenceClass.getSimpleName()).getResultList();
    }

    /**
     * Find by id of persistence class
     *
     * @param id
     * @return
     */
    public T findById(ID id) {
        logger.debug("FindById called by: " + persistenceClass.getSimpleName() + ", mit id: " + id);
        return getEntityManager().find(persistenceClass, id);
    }

    /**
     * Persist entity of persistence class
     *
     * @param entity
     * @return
     */
    public T makePersistent(T entity) {
        logger.debug("Try to save entity of: " + persistenceClass.getSimpleName());
        EntityManager entityManager = getEntityManager();
        entityManager.persist(entity);
        entityManager.flush();
        entityManager.refresh(entity);
        return entity;
    }

    public T makePersistentByMerge(T entity) {
        logger.debug("Try to save entity of: " + persistenceClass.getSimpleName());
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return entity;
    }

    public void makeTransient(T entity) {
//        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(entity);
        getEntityManager().getTransaction().commit();
        System.out.println("Entity sollte gelöscht werden");
    }

    public void flush() {
        getEntityManager().flush();
    }

    public EntityManager getEntityManager() {
        return DatabaseConnection.currentEntityManager();
    }

    /**
     * Search by value of attribute from persistence class
     *
     * @param attribut
     * @param searchString
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> makeQuerySearchByString(String attribute, String searchString) {
        logger.debug("Sarch by value: " + searchString + " of attribute " + attribute + " from "
                     + persistenceClass.getSimpleName());
        Query q = getEntityManager().createQuery("from " + persistenceClass.getSimpleName() + " where " + attribute
                                                 + " like :search");
        q.setParameter("search", searchString + "%");
        return q.getResultList();
    }

    // ************ TESTMETHODE DST ***********************
    @SuppressWarnings("unchecked")
    public List<T> makeQuerySearchByString1(String attribute) {
        System.out.println("Dynamischer Query Abfrage");
        Query q = getEntityManager().createQuery("from " + persistenceClass.getSimpleName() + " where " + attribute);
        // Query q = entityManager.createQuery("from " + persistenceClass.getSimpleName() + " where stampingtime between '2016-08-21 13:00:00' and
        // '2016-08-21 18:00:00' ");
        return q.getResultList();
    }
}