package com.andrey;

import com.andrey.dao.PaymentRepository;
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

            try(Session session = sessionFactory.openSession()) {
                session.beginTransaction();

//                LazyInitializationException
                PaymentRepository paymentRepository = new PaymentRepository(sessionFactory);
                paymentRepository.findById(1L).ifPresent(System.out::println);

                session.getTransaction().commit();
            }
        }
    }

}
