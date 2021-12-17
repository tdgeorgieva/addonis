package com.addonis.repositories.IDE;

import com.addonis.models.IDE;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class IDERepositoryImpl extends AbstractCRUDRepository<IDE> implements IDERepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public IDERepositoryImpl(SessionFactory sessionFactory) {
        super(IDE.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }


}
