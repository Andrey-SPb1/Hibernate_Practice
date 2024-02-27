package com.andrey;

import com.andrey.entity.*;
import com.andrey.interceptor.GlobalInterceptor;
import com.andrey.util.HibernateUtil;
import com.andrey.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory
                    .withOptions()
                    .interceptor(new GlobalInterceptor())
                    .openSession()) {
            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.remove(payment);

            session.getTransaction().commit();
        }
    }

}
