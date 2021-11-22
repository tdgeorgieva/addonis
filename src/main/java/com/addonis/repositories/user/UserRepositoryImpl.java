package com.addonis.repositories.user;

import com.addonis.models.BlockedUser;
import com.addonis.models.User;
import com.addonis.models.UserStatus;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> search(Optional<String> search) {
        try (Session session = sessionFactory.openSession()) {
            //by phone number, username or email
            Query<User> query = session.createQuery(
                    "from User where phoneNumber like :name or username like :name or email like :name", User.class);
            query.setParameter("name", "%" + search.get() + "%");

            return query.list();
        }
    }

    @Override
    public void blockUser(int id, int adminId) {
        try (Session session = sessionFactory.openSession()) {
            BlockedUser newBlockedUser = new BlockedUser(id, adminId);
            session.save(newBlockedUser);
            getById(id).setStatus(UserStatus.BLOCKED);
        }
    }
}
