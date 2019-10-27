package org.sdk.interfaces;


import org.sdk.EBIHibernateSessionPooling;
import org.hibernate.Session;

public interface IEBIHibernate {

    void openHibernateSession(String Name);

    void removeAllHibernateSessions();

    boolean removeHibernateSession(String index);

    Session session(String index);

    EBIHibernateSessionPooling transaction(String index);

    void commit();

    void begin();

    boolean isActive();

    void rollback();
}
