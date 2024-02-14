package com.andrey;

import com.andrey.entity.Company;
import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import com.andrey.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.enableFetchProfile("withCompanyAndPayments");

            User user = session.get(User.class, 1L);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getPayments().size());

//            List<User> users = session.createQuery("select u from User u " +
//                            "join fetch u.payments " +
//                            "join fetch u.company ", User.class)
//                    .list();
//            users.forEach(user -> System.out.println(user.getPayments().size()));
//            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
