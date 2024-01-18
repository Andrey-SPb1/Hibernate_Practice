package com.andrey;

import com.andrey.entity.Birthday;
import com.andrey.entity.PersonalInfo;
import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .username("petr123")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDay(new Birthday(LocalDate.of(2000, 12, 15)))
                        .build())
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
