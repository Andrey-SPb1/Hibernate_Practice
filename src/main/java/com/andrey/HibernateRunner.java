package com.andrey;

import com.andrey.entity.*;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;

import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            User user = null;

            try(Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                User user1 = session.find(User.class, 1L);

                List<Payment> payments = session.createQuery("select p from Payment p where receiver.id = :userId"
                                , Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
//                        .setCacheRegion("queries")
//                        .setHint(QueryHints.CACHEABLE, true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }

            try(Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                List<Payment> payments = session.createQuery("select p from Payment p where receiver.id = :userId"
                                , Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .getResultList();

                System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
                session.getTransaction().commit();
            }
        }
    }

}
