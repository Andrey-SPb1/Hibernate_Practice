package com.andrey.dao;

import com.andrey.entity.Company;

import javax.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Integer, Company> {
    public CompanyRepository(EntityManager entityManager) {
        super(entityManager, Company.class);
    }
}
