package com.addonis.repositories;

import com.addonis.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static java.lang.String.format;

public abstract class AbstractReadRepository<T> implements BaseReadRepository<T> {

    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public AbstractReadRepository(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Retrieves an entity from the database that has a <code>field</code> equal to <code>value</code>.
     * <br/>
     * Example: <code>getByField("id", 1, Parcel.class)</code>
     * will execute the following HQL: <code>from Parcel where id = 1;</code>
     *
     * @param name  the name of the field
     * @param value the value of the field
     * @return an entity that matches the given criteria
     */
    @Override
    public <V> T getByField(String name, V value) {
        final String query = format("from %s where %s = :value", clazz.getSimpleName(), name);
        final String notFoundErrorMessage = format("%s with %s %s not found", clazz.getSimpleName(), name, value);

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", value)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException(notFoundErrorMessage));
        }
    }

    @Override
    public T getById(int id) {
        return getByField("id", id);
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s ", clazz.getName()), clazz).list();
        }
    }
}
