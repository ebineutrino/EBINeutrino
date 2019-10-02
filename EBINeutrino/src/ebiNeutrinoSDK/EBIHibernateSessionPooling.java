package ebiNeutrinoSDK;

import ebiNeutrinoSDK.interfaces.IEBIHibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeMap;


/**
 * Hibernate session client pooling class
 */
public class EBIHibernateSessionPooling implements IEBIHibernate {

    private TreeMap<String, Object> sessionList = null;
    public Configuration cfg = null;
    private Transaction actualTransaction = null;
    private SessionFactory sessionFactory = null;

    public EBIHibernateSessionPooling(final Configuration cfg) {
        this.cfg = cfg;
        sessionList = new TreeMap();
        final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
       
        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
    }

    @Override
	public void openHibernateSession(final String Name) {
        if (!sessionList.containsKey(Name)) {
            final Object[] sessionObject = new Object[2];
            sessionObject[0] = sessionFactory.openSession();
            sessionObject[1] = ((Session) sessionObject[0]).beginTransaction();
            sessionList.put(Name, sessionObject);
        }
    }

    @Override
	public void removeAllHibernateSessions() {
        if (sessionList != null || sessionList.size() > 0) {
            final Iterator iter = sessionList.keySet().iterator();

            while (iter.hasNext()) {
                final String alias = (String) iter.next();

                try {
                    if (((Object[]) sessionList.get(alias))[1] != null &&
                            ((Object[]) sessionList.get(alias))[0] != null) {
                        final Transaction act = ((Transaction) ((Object[]) sessionList.get(alias))[1]);
                        final Session ses = ((Session) ((Object[]) sessionList.get(alias))[0]);

                        if (act.isActive()) {
                            if (EBISystem.getInstance().iDB().getActiveConnection().getAutoCommit()) {
                                EBISystem.getInstance().iDB().getActiveConnection().setAutoCommit(false);
                            }
                            act.rollback();
                        }

                        if (ses.isOpen()) {
                            ses.close();
                        }
                    }
                } catch (final SQLException x) {
                   x.printStackTrace();
                } catch (final HibernateException ex) {
                   ex.printStackTrace();
                }
            }
            sessionList.clear();
        }
    }

    @Override
	public boolean removeHibernateSession(final String index) {
        if (((Object[]) sessionList.get(index))[0] != null) {
            ((Session) ((Object[]) sessionList.get(index))[0]).close();
            sessionList.remove(index);
        } else {
            return false;
        }
        return true;
    }

    @Override
	public Session session(final String index) {
        try {
            if (sessionList.get(index) == null ||
                    !((Session) ((Object[]) sessionList.get(index))[0]).isConnected() ||
                    !((Session) ((Object[]) sessionList.get(index))[0]).isOpen() ||
                    !EBISystem.getInstance().iDB().isValidConnection()) {
                openHibernateSession(index);
            }

        } catch (final Exception ex) {
        }
        return ((Session) ((Object[]) sessionList.get(index))[0]);
    }

    @Override
	public EBIHibernateSessionPooling transaction(final String index) {
        try {
            if (sessionList.get(index)== null || !EBISystem.getInstance().iDB().isValidConnection()) {
                openHibernateSession(index);
            }
            actualTransaction = ((Transaction) ((Object[]) sessionList.get(index))[1]);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    @Override
	public void commit(){
        if(actualTransaction.isActive()){
            actualTransaction.commit();
        }
    }

    @Override
	public void begin(){
        if (actualTransaction.isActive()) {
            actualTransaction.commit();
        }
        actualTransaction.begin();
    }

    @Override
	public boolean isActive(){
       return actualTransaction.isActive();
    }

    @Override
	public void rollback(){
        actualTransaction.rollback();
    }

}
