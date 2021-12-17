package com.addonis.repositories.addon;

import com.addonis.models.addon.AddonPage;
import com.addonis.models.addon.AddonSearchCriteria;
import com.addonis.models.addon.Addon;
import com.addonis.models.addon.AddonStatus;
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
import java.util.stream.Collectors;

@Repository
public class AddonRepositoryImpl extends AbstractCRUDRepository<Addon> implements AddonRepository {

    private final SessionFactory sessionFactory;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    
    public AddonRepositoryImpl(SessionFactory sessionFactory, EntityManager entityManager) {
        super(Addon.class, sessionFactory);
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    public Page<Addon> findAllWithFilters(AddonPage addonPage, AddonSearchCriteria addonSearchCriteria, boolean isAdmin) {

        CriteriaQuery<Addon> criteriaQuery = criteriaBuilder.createQuery(Addon.class);
        Root<Addon> addonRoot = criteriaQuery.from(Addon.class);
        Predicate predicate = getPredicate(addonSearchCriteria, addonRoot, isAdmin);
        criteriaQuery.where(predicate);
        setOrder(addonPage, criteriaQuery, addonRoot);

        TypedQuery<Addon> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(addonPage.getPageNumber() * addonPage.getPageSize());
        typedQuery.setMaxResults(addonPage.getPageSize());

        Pageable pageable = getPageable(addonPage);

        long addonsCount = getAddonsCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, addonsCount);

    }

    private long getAddonsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Addon> countRoot = countQuery.from(Addon.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Pageable getPageable(AddonPage addonPage) {
        if (!addonPage.getSortBy().equals("")) {
            String[] parts = addonPage.getSortBy().split("_");
            if (parts.length != 2) {
                //todo:handle
            }
            String sortAttr = parts[0];
            String direction = parts[1];
            Sort sort = Sort.by(direction, sortAttr);
            return PageRequest.of(addonPage.getPageNumber(), addonPage.getPageSize(), sort);
        }
        Sort sort = Sort.by("asc", "name");
        return PageRequest.of(addonPage.getPageNumber(), addonPage.getPageSize(), sort);
    }

    private void setOrder(AddonPage addonPage, CriteriaQuery<Addon> criteriaQuery, Root<Addon> addonRoot) {
        if (!addonPage.getSortBy().equals("")) {
            String[] parts = addonPage.getSortBy().split("_");
            if (parts.length != 2) {
                //todo:handle
            }
            String sortAttr = parts[0];
            String direction = parts[1];
            if(direction.equals("asc")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(addonRoot.get(sortAttr)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(addonRoot.get(sortAttr)));
            }
        }
    }

    private Predicate getPredicate(AddonSearchCriteria addonSearchCriteria, Root<Addon> addonRoot, boolean isAdmin) {
        List<Predicate> predicates = new ArrayList<>();
        if(!isAdmin) {
            predicates.add(
                    criteriaBuilder.equal(addonRoot.get("status"), AddonStatus.APPROVED)
            );
        }
        if(Objects.nonNull(addonSearchCriteria.getAddonName())) {
            predicates.add(
                    criteriaBuilder.like(addonRoot.get("name"), "%" + addonSearchCriteria.getAddonName() + "%")
            );
        }
        if(Objects.nonNull(addonSearchCriteria) && addonSearchCriteria.getIdeId() != -1) {
            predicates.add(
                    criteriaBuilder.equal(addonRoot.get("ide"), addonSearchCriteria.getIdeId())
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    @Override
    public List<Addon> search(Optional<String> search) {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery(
                    "from Addon where name like :name or user.username like :name or ide.name like :name", Addon.class);
            query.setParameter("name", "%" + search.get() + "%");

            return query.list();
        }
    }

    @Override
    public List<Addon> filter(Optional<String> name, Optional<Integer> ideId, Optional<String> sort) {
        try (Session session = sessionFactory.openSession()) {
            var queryString = new StringBuilder("from Addon ");
            var filters = new ArrayList<String>();
            var params = new HashMap<String, Object>();

            name.ifPresent(value -> {
                if (!value.equals("")) {
                    filters.add(" name = :name ");
                    params.put("name", value);
                }
            });

            ideId.ifPresent(value -> {
                if (value != -1) {
                    filters.add(" ide.id = :ideId ");
                    params.put("ideId", value);
                }
            });

            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }

            sort.ifPresent(value -> {
                if (!value.equals("")) {
                    queryString.append(generateQueryStringFromSortParam(value));
                }
            });

            Query<Addon> query = session.createQuery(queryString.toString(), Addon.class);
            query.setProperties(params);

            return query.list();
        }

    }

    @Override
    public List<Addon> sortAddonsByDownloads() {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery(" from Addon " + generateQueryStringFromSortParam("downloads_descending"), Addon.class);
            return query.list();
        }
    }

    @Override
    public List<Addon> getFeaturedAddons() {
        try (Session session = sessionFactory.openSession()) {
            var queryString = new String(" from Addon where is_featured = 1 and status = 3");
            Query<Addon> query = session.createQuery(queryString, Addon.class);

            return query.list();
        }
    }

    @Override
    public List<Addon> getUnapprovedAddons() {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery("from Addon where status = 1 or status = 2", Addon.class);
            return query.list();
        }
    }

    @Override
    public List<Addon> getNewAddons() {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery(" from Addon where status = 3 " + generateQueryStringFromSortParam("upload_descending"), Addon.class);

            //todo
            List<Addon> newAddons = query.list();
            return newAddons.stream().limit(10).collect(Collectors.toList());
        }
    }

    @Override
    public List<Addon> getPopularAddons() {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery(" from Addon where status = 3" + generateQueryStringFromSortParam("downloads_descending"), Addon.class);

            List<Addon> popularAddons = query.list();
            return popularAddons.stream().limit(10).collect(Collectors.toList());
        }
    }

    @Override
    public List<Addon> getAddonsByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery("from Addon where user.id = :userId and status != 0", Addon.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Addon> getDraftsByUserId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Addon> query = session.createQuery("from Addon where user.id = :userId and status = 0", Addon.class);
            query.setParameter("userId", id);
            return query.list();
        }
    }

    @Override
    public List<Addon> sortAddonsByLastCommitDate(List<Addon> list) {
        list.sort(Comparator.comparing(Addon::getLastCommitDate).reversed());
        return list;
    }

    @Override
    public void updateAddon(Addon addon) {

    }

    private String generateQueryStringFromSortParam(String sort) {

        switch (sort) {
            case "name_ascending":
                return " order by name asc";
            case "name_descending":
                return " order by name desc";
            case "downloads_ascending":
                return " order by downloads_count asc";
            case "downloads_descending":
                return " order by downloads_count desc";
            case "upload_ascending":
                return " order by upload_date asc";
            case "upload_descending":
                return " order by upload_date desc";
            case "last_commit_date":
                return " order by last_commit_date desc";
        }
        return "Did not enter switch case.";

    }


}
