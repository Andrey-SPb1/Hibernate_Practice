package com.andrey;

import com.andrey.converter.BirthdayConverter;
import com.andrey.entity.Birthday;
import com.andrey.entity.Role;
import com.andrey.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {

        Configuration configuration = new Configuration();
//        configuration.addAnnotatedClass(User.class);
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.configure();

        try(SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("ivan123")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDay(new Birthday(LocalDate.of(1997, 8, 14)))
                    .age(new Birthday(LocalDate.of(1997, 8, 14)).getAge())
                    .role(Role.ADMIN)
                    .build();

            session.persist(user);

            session.getTransaction().commit();
        }

    }

}
