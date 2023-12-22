package com.andrey;

import com.andrey.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(User.class);
        configuration.configure();

        try(SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("ivan123")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDay(LocalDate.of(1997, 8, 14))
                    .age(26)
                    .build();

            session.persist(user);

            session.getTransaction().commit();
        }

    }

}
