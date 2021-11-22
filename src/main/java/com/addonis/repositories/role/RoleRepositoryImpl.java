package com.addonis.repositories.role;

import com.addonis.models.Role;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl
        extends AbstractCRUDRepository<Role> implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(Role.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
