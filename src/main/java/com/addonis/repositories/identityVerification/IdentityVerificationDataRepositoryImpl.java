package com.addonis.repositories.identityVerification;

import com.addonis.models.IdentityVerificationData;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class IdentityVerificationDataRepositoryImpl extends AbstractCRUDRepository<IdentityVerificationData> implements IdentityVerificationDataRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public IdentityVerificationDataRepositoryImpl(SessionFactory sessionFactory) {
        super(IdentityVerificationData.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

}
