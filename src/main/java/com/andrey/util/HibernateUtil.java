package com.andrey.util;

import com.andrey.converter.BirthdayConverter;
import com.andrey.interceptor.GlobalInterceptor;
import com.andrey.listener.AuditTableListener;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(User.class);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.setInterceptor(new GlobalInterceptor());
        configuration.configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory();
//        registerListeners(sessionFactory);

        return sessionFactory;
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry service = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        AuditTableListener auditTableListener = new AuditTableListener();
        service.appendListeners(EventType.PRE_INSERT, auditTableListener);
        service.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }

}
