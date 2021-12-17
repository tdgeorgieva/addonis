package com.addonis.repositories.user;

import com.addonis.models.user.User;
import com.addonis.models.user.UserPage;
import com.addonis.models.user.UserSearchCriteria;
import com.addonis.models.addon.Addon;
import com.addonis.repositories.AbstractCRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {

    private final SessionFactory sessionFactory;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory, EntityManager entityManager) {
        super(User.class, sessionFactory);
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Page<User> findAllWithFilters(UserPage userPage, UserSearchCriteria userSearchCriteria) {
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate predicate = getPredicate(userSearchCriteria, userRoot);
        criteriaQuery.where(predicate);
        setOrder(userPage, criteriaQuery, userRoot);

        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(userPage.getPageNumber() * userPage.getPageSize());
        typedQuery.setMaxResults(userPage.getPageSize());

        Pageable pageable = getPageable(userPage);

        long usersCount = getUsersCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);
    }

    private long getUsersCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Pageable getPageable(UserPage userPage) {
        if (!userPage.getSortBy().equals("")) {
            String[] parts = userPage.getSortBy().split("_");
            if (parts.length != 2) {
                //todo:handle
            }
            String sortAttr = parts[0];
            String direction = parts[1];
            Sort sort = Sort.by(direction, sortAttr);
            return PageRequest.of(userPage.getPageNumber(), userPage.getPageSize(), sort);
        }
        Sort sort = Sort.by("asc", "name");
        return PageRequest.of(userPage.getPageNumber(), userPage.getPageSize(), sort);
    }

    private void setOrder(UserPage userPage, CriteriaQuery<User> criteriaQuery, Root<User> userRoot) {
        if (!userPage.getSortBy().equals("")) {
            String[] parts = userPage.getSortBy().split("_");
            if (parts.length != 2) {
                //todo:handle
            }
            String sortAttr = parts[0];
            String direction = parts[1];
            if(direction.equals("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get(sortAttr)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(userRoot.get(sortAttr)));
            }
        }
    }

    private Predicate getPredicate(UserSearchCriteria userSearchCriteria, Root<User> userRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(userSearchCriteria.getName())) {
            predicates.add(
                    criteriaBuilder.or(
                            criteriaBuilder.like(userRoot.get("username"), "%" + userSearchCriteria.getName() + "%"),
                            criteriaBuilder.like(userRoot.get("email"), "%" + userSearchCriteria.getName() + "%"),
                            criteriaBuilder.like(userRoot.get("phoneNumber"), "%" + userSearchCriteria.getName() + "%")
                    )
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    public List<User> search(Optional<String> search) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where phoneNumber like :name or username like :name or email like :name", User.class);
            query.setParameter("name", "%" + search.get() + "%");

            return query.list();
        }
    }

    @Override
    public List<User> filter(String searchAll) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> filter = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            if (searchAll != null && !searchAll.equals("")) {
                filter.add(" username like :username ");
                filter.add(" email like :email ");
                filter.add(" phone_number like :phoneNumber ");

                params.put("username", "%" + searchAll + "%");
                params.put("email", "%" + searchAll + "%");
                params.put("phoneNumber", "%" + searchAll + "%");

                stringBuilder.append(" from User where ").append(String.join(" or ", filter));
            } else {
                stringBuilder.append(" from User ");
            }

            Query<User> query = session.createQuery(stringBuilder.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Addon> getUserAddons(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery("from Addon where user.id = :id", Addon.class);
            query.setParameter("id", id);

            return query.list();
        }
    }


}
