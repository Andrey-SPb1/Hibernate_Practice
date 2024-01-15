package com.andrey;

import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {

    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {

        User user = User.builder()
                .username("ivan123")
                .firstname("Ivan")
                .lastname("Ivanov")
                .build();
        log.info("User entity is in transient state, object - {}", user);

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try(session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction has been created, object - {}", transaction);

                session1.merge(user);
                log.trace("User entity is in persistent state: {}, session: {}", user, session1);

                session1.getTransaction().commit();
            }
            log.warn("User entity is in detached state: {}, session is closed: {}", user, session1);
        } catch (Exception exception) {
            log.error("Exception occurred", exception);
            throw exception;
        }
    }
}
