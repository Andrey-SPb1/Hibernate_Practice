package com.andrey.dao;

import com.andrey.entity.Company;
import org.hibernate.SessionFactory;

public class CompanyRepository extends RepositoryBase<Integer, Company> {
    public CompanyRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Company.class);
    }
}
