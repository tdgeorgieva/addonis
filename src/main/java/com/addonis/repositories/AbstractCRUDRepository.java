package com.addonis.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractCRUDRepository<T> extends AbstractReadRepository<T> implements BaseCRUDRepository<T> {

    private final SessionFactory sessionFactory;

    public AbstractCRUDRepository(Class<T> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        T toDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();
        }
    }
}
