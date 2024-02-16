package com.andrey;

import com.andrey.entity.Payment;
import com.andrey.entity.Profile;
import com.andrey.entity.User;
import com.andrey.util.HibernateUtil;
import com.andrey.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
