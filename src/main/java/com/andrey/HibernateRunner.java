package com.andrey;

import com.andrey.entity.Payment;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.LockModeType;
//import javax.persistence.lock.timeout;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {

//            session.setDefaultReadOnly(true);
//            session.setReadOnly();
            session.beginTransaction();

            session.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

//            session.createQuery("select p from Payment p")
//                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//                    .setHint("javax.persistence.lock.timeout", 5000)
//                    .setTimeout(5000)
//                    .setReadOnly(true)
//                    .list();

            Payment payment = session.find(Payment.class, 1L);
//            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
