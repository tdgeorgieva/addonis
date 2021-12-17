package com.addonis.repositories.addon;

import com.addonis.exceptions.EntityNotFoundException;
import com.addonis.models.user.User;
import com.addonis.models.addon.Rating;
import com.addonis.repositories.user.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class RatingRepositoryImpl implements RatingRepository {

    private final SessionFactory sessionFactory;
    private final UserRepository userRepository;

    public RatingRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository) {
        this.sessionFactory = sessionFactory;
        this.userRepository = userRepository;
    }

    @Override
    public Rating ratingExistsForAddonEntity(int id, int addonId) {
        try (Session session = sessionFactory.openSession()) {
            User user = userRepository.getById(id);
            Query<Rating> query = session.createQuery(" from Rating where user.id = :id and addon.id = :addonId ", Rating.class);
            query.setParameter("addonId", addonId);
            query.setParameter("id", id);
            if(query.list().size() == 0) {
                return null;
            }
            return query.list().get(0);
        }
    }

    @Override
    public Rating getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Rating rating = session.get(Rating.class, id);
            if (rating == null) {
                throw new EntityNotFoundException("Rating", id);
            }
            return rating;
        }
    }

    @Override
    public void create(Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            session.save(rating);
        }
    }

    @Override
    public void update(Rating rating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(rating);
            session.getTransaction().commit();
        }
    }

    @Override
    public double calculateRating(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query = session.createQuery(String.format("SELECT avg(rating) from Rating where addon_id = :id",  Rating.class));
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NullPointerException e) {
            return 0;
        }
    }
}
