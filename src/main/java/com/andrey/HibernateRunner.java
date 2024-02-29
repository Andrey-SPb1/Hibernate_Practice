package com.andrey;

import com.andrey.entity.*;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

                session.getTransaction().commit();
            }

            try(Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                session.getTransaction().commit();
            }
        }
    }

}
