package com.andrey;

import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .username("ivan123")
                .firstname("Ivan")
                .lastname("Ivanov")
                .build();

        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try(Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                session1.persist(user);
//                session1.remove(user);

                session1.getTransaction().commit();
            }

            try(Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                session2.update(user);
//                session2.remove(user);
                user.setLastname("Petrov");

                session2.refresh(user);

                session2.getTransaction().commit();
            }
        }
    }
}
