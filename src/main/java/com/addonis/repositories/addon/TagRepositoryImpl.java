package com.addonis.repositories.addon;

import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.addon.AddonTag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<AddonTag> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<AddonTag> query = session.createQuery("from AddonTag", AddonTag.class);
            return query.list();
        }
    }

    @Override
    public AddonTag getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            AddonTag tag = session.get(AddonTag.class, id);
            if (tag == null) {
                throw new EntityNotFoundException("AddonTag", id);
            }
            return tag;
        }
    }

    @Override
    public void create(AddonTag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.save(tag);
        }
    }

    @Override
    public void delete(int id) {
        AddonTag tagToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(tagToDelete);
            session.getTransaction().commit();
        }
    }
}
