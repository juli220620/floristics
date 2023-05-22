package com.gitlab.juli220620.dao.repo;

import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@RequiredArgsConstructor
public abstract class HibernateRepo<E, I> implements CrudRepo<E, I> {

    protected final SessionFactory factory;

    public E save(E entity) {
        try (Session session = factory.openSession()) {
            session.persist(entity);
            return entity;
        }
    }

    public E findById(I id) {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery(findByIdQuery(), getEntityClass());
            query.setParameter("id", id);
            return (E) query.getSingleResult();
        }
    }

    public List<E> getAll() {
        try (Session session = factory.openSession()) {
            Query query = session.createQuery(getAllQuery(), getEntityClass());
            return (List<E>) query.getResultList();
        }
    }

    public E update(E entity) {
        try (Session session = factory.openSession()) {
            session.persist(session.merge(entity));
            return entity;
        }
    }

    public boolean delete(E entity) {
        try (Session session = factory.openSession()) {
            session.remove(entity);
            return true;
        }
    }

    protected abstract String findByIdQuery();

    protected abstract String getAllQuery();

    protected abstract Class<E> getEntityClass();
}
