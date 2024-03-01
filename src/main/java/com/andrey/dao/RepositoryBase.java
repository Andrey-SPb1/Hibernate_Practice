package com.andrey.dao;

import com.andrey.entity.BaseEntity;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final SessionFactory sessionFactory;

    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        @Cleanup Session session = sessionFactory.openSession();
        session.remove(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        @Cleanup Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(clazz ,id));
    }

    @Override
    public List<E> findAll() {
        @Cleanup Session session = sessionFactory.openSession();
        CriteriaQuery<E> query = session.getCriteriaBuilder().createQuery(clazz);
        query.from(clazz);
        return session.createQuery(query)
                .getResultList();
    }
}
