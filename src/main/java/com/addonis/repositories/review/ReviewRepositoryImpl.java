package com.addonis.repositories.review;

import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ReviewRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Review> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Review> query = session.createQuery("from Review", Review.class);
            return query.list();
        }
    }

    @Override
    public Review getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Review review = session.get(Review.class, id);
            if (review == null) {
                throw new EntityNotFoundException("Review", id);
            }
            return review;
        }
    }

    @Override
    public List<Review> getByUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Review> query = session.createQuery("from Review where user.id = :id", Review.class);
            query.setParameter("id", userId);
            return query.list();
        }
    }

    @Override
    public void deleteByAddon(int addonId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Review> query = session.createQuery("from Review where addon.id = :id", Review.class);
            query.setParameter("id", addonId);

            for (Review review : query.list()) {
                delete(review.getId());
            }
        }
    }

    @Override
    public void create(Review review) {
        try (Session session = sessionFactory.openSession()) {
            session.save(review);
        }
    }

    @Override
    public void delete(int id) {
        Review reviewToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(reviewToDelete);
            session.getTransaction().commit();
        }

    }

    @Override
    public List<Review> getByAddon(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Review> query = session.createQuery("from Review where addon_id = :id", Review.class);
            query.setParameter("id", id);
            return query.list();
        }
    }
}
