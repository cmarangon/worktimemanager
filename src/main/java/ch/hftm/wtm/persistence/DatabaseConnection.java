package ch.hftm.wtm.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * Repräsentiert die Verbindung zur Datenbank.
 *
 * @author HOEFI
 * @since 06.06.2016
 * @version 1.0
 */
public class DatabaseConnection {

    /**
     * Embedded HSQL-Server
     */
    private static final EmbeddedHSQL embeddedHSQL = new EmbeddedHSQL();
    /**
     * Globale und Thread-sichere EntityManagerFactorys
     */
    private static final EntityManagerFactory emFactory;
    private static final EntityManagerFactory emFactory_Test;
    /**
     * Die ThreadLocal Instanz bindet die EntityManager Instanzen an den aktuellen Thread.
     */
    public static final ThreadLocal<EntityManager> session = new ThreadLocal<EntityManager>();
    public static final ThreadLocal<EntityManager> session_Test = new ThreadLocal<EntityManager>();

    private static boolean isTestDatabase = false;
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    static {
        try {
           logger.info("Start DB-Server");
           embeddedHSQL.startServer();
           System.out.println("Verbinde zu DBs");
            logger.info("Create EntityManagerFactory - Production");
            emFactory = Persistence.createEntityManagerFactory("WorkTimeManager");
            System.out.println("PROD OK");
            logger.info("Create EntityManagerFactory - Test");
            emFactory_Test = Persistence.createEntityManagerFactory("WorkTimeManager_Test");
            System.out.println("TEST OK");
        } catch (Throwable ex) {
            logger.error("Initialisation EntityManagerFactory failed.");
            logger.error(ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Öffnet einen neuen EntityManager falls der aktuelle Thread noch keinen besitzt.
     * 
     * @author HOEFI
     * @since 06.06.2016
     * @return
     * @throws HibernateException
     */
    public static EntityManager currentEntityManager() throws HibernateException {
        logger.info("Retrieve current EntityManager. ThreadId=" + Thread.currentThread().getId());
        EntityManager em = (EntityManager) getSession().get();
        if (em == null) {
            logger.info("Create new EntityManager. ThreadId=" + Thread.currentThread().getId());
            em = getEmFactory().createEntityManager();
            getSession().set(em);
        }
        return em;
    }

    /**
     * Schliesst den aktuellen EntityManager (Produktiv und Testumgebung) des aktuellen Threads.
     * 
     * @author HOEFI
     * @since 06.06.2016
     */
    public static void closeEntityManager() {
        logger.info("Close current EntityManager. ThreadId=" + Thread.currentThread().getId());
        EntityManager em = (EntityManager) session.get();
        session.set(null);
        if (em != null) em.close();
        
        logger.info("Close current TestEntityManager. ThreadId=" + Thread.currentThread().getId());
        em = (EntityManager) session_Test.get();
        session_Test.set(null);
        if (em != null) em.close();
    }
    
    public static void setTestDatabase(boolean bValue) {
        isTestDatabase = bValue;
    }
    
    public static boolean isTestDatabase() {
        return isTestDatabase;
    }
    
    private static ThreadLocal<EntityManager> getSession() {
        if (!isTestDatabase)
            return session;
        else
            return session_Test;
    }
    
    private static EntityManagerFactory getEmFactory() {
        if (!isTestDatabase)
            return emFactory;
        else
            return emFactory_Test;
    }
    
    public static void beginTransaction() {
        EntityManager em = currentEntityManager();
        EntityTransaction et = em.getTransaction();
        if (!et.isActive())
            et.begin();
    }
    
    public static void commitTransaction() {
        EntityManager em = currentEntityManager();
        EntityTransaction et = em.getTransaction();
        if (et.isActive())
            et.commit();
    }
    
    public static void rollbackTransaction() {
        EntityManager em = currentEntityManager();
        EntityTransaction et = em.getTransaction();
        if (et.isActive())
            et.rollback();
    }

}
