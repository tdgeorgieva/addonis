package com.addonis.repositories.addon;

import com.addonis.models.addon.AddonCode;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AddonCodeRepositoryImpl
        extends AbstractCRUDRepository<AddonCode>
        implements AddonCodeRepository {

    private final SessionFactory sessionFactory;

    public AddonCodeRepositoryImpl(SessionFactory sessionFactory) {
        super(AddonCode.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AddonCode findByCode(String code) {
        return getByField("code", code);
    }

    @Scheduled(cron = "0 0 6 * * *")
    @Override
    public void deleteAutomatically() {
        Session session = this.sessionFactory.getCurrentSession();
        LocalDate today = LocalDate.now();
        Query<AddonCode> query = session.createQuery("from AddonCode where AddonCode.expirationDate = :today", AddonCode.class);
        query.setParameter("today", today);
        List<AddonCode> expired = query.list();
        expired.forEach(session::delete);
        session.flush();
    }
}
