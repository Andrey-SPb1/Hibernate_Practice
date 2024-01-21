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
                .name("Facebook")
                .build();

        User user = User.builder()
                .username("ivan1234")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Ivan")
                        .lastname("Ivanov")
                        .birthDay(new Birthday(LocalDate.of(2000, 12, 15)))
                        .build())
                .company(google)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                session1.beginTransaction();

                session1.persist(user);

                session1.getTransaction().commit();
            }
        }
    }
}
