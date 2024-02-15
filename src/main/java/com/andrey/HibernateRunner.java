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
            Session session = sessionFactory.openSession();
            Session session1 = sessionFactory.openSession()) {

            session.beginTransaction();
            session1.beginTransaction();

            session.createQuery("select p from Payment p")
                    .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//                    .setHint("javax.persistence.lock.timeout", 5000)
//                    .setTimeout(5000)
                    .list();

//            Payment payment = session.find(Payment.class, 1L, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
//            payment.setAmount(payment.getAmount() + 10);

            Payment theSamePayment = session1.find(Payment.class, 1L);
//            theSamePayment.setAmount(theSamePayment.getAmount() + 20);

            session1.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}
