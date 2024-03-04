package com.andrey.dao;

import com.andrey.entity.User;

import javax.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }

}
