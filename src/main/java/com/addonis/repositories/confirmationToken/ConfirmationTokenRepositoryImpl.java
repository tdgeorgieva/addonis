package com.addonis.repositories.confirmationToken;

import com.addonis.models.ConfirmationToken;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmationTokenRepositoryImpl
        extends AbstractCRUDRepository<ConfirmationToken>
        implements ConfirmationTokenRepository {

    private final SessionFactory sessionFactory;

    public ConfirmationTokenRepositoryImpl(SessionFactory sessionFactory) {
        super(ConfirmationToken.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ConfirmationToken findByConfirmationToken(String confirmationToken) {
        return getByField("confirmationToken", confirmationToken);
    }
}
