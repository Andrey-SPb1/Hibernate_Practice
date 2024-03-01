package com.andrey.dao;

import com.andrey.entity.Payment;
import org.hibernate.SessionFactory;

public class PaymentRepository extends RepositoryBase<Long, Payment> {
    public PaymentRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Payment.class);
    }
}
