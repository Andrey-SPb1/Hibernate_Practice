package com.andrey;

import com.andrey.entity.Birthday;
import com.andrey.entity.Company;
import com.andrey.entity.PersonalInfo;
import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        Company google = Company.builder()
                .name("Google")
                .build();

        User user = User.builder()
                .username("petr123")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .birthDay(new Birthday(LocalDate.of(2000, 12, 15)))
                        .build())
                .company(google)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                session1.beginTransaction();

//                session1.persist(google);
//                session1.persist(user);
                User user1 = session1.get(User.class, 1L);

                session1.getTransaction().commit();
            }
        }
    }
}
