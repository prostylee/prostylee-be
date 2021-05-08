package vn.prostylee.core.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import vn.prostylee.core.repository.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;

/**
 * Custom Repository with soft delete features
 * @param <T> The type of entity
 * @param <ID> The primary key of entity
 */
@Transactional
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private static final String DELETED_FIELD = "deletedAt";
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final Class<T> domainClass;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.domainClass = domainClass;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Iterable<T> findAllActive() {
        return super.findAll(notDeleted());
    }

    @Override
    public Iterable<T> findAllActive(Sort sort) {
        return super.findAll(notDeleted());
    }

    @Override
    public Page<T> findAllActive(Pageable pageable) {
        return super.findAll(notDeleted(), pageable);
    }

    @Override
    public Page<T> findAllActive(Specification<T> specs, Pageable pageable) {
        if (specs == null) {
            return super.findAll(notDeleted(), pageable);
        }
        return super.findAll(Specification.where(specs.and(new DeletedIsNull<>())), pageable);
    }

    @Override
    public Iterable<T> findAllActive(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        if (entityInformation.hasCompositeId()) {
            List<T> results = new ArrayList<>();

            for (ID id : ids) {
                results.add(findOneActive(id).get());
            }
            return results;
        }

        ByIdsSpecification<T> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<T> query = getQuery(Specification.where(specification).and(notDeleted()), (Sort) null);

        return query.setParameter(specification.parameter, ids).getResultList();
    }

    @Override
    public Optional<T> findOneActive(ID id) {
        return super.findOne(
                Specification.where(new ByIdSpecification<>(entityInformation, id)).and(notDeleted()));
    }

    @Override
    public int softDelete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return softDelete(id, new Date());
    }

    @Override
    public int softDelete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        return softDelete(entity, new Date());
    }

    @Override
    public int softDelete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        int count = 0;
        for (T entity : entities) {
            count += softDelete(entity);
        }
        return count;
    }

    @Override
    public int softDeleteAll() {
        int count = 0;
        for (T entity : findAllActive()) {
            count += softDelete(entity);
        }
        return count;
    }

    @Override
    public int scheduleSoftDelete(ID id, Date dateTime) {
        return softDelete(id, dateTime);
    }

    @Override
    public int scheduleSoftDelete(T entity, Date dateTime) {
        return softDelete(entity, dateTime);
    }

    private int softDelete(ID id, Date dateTime) {
        Assert.notNull(id, "The given id must not be null!");

        T entity = findOneActive(id).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1));

        return softDelete(entity, dateTime);
    }

    private int softDelete(T entity, Date dateTime) {
        Assert.notNull(entity, "The entity must not be null!");

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaUpdate<T> update = cb.createCriteriaUpdate(domainClass);

        Root<T> root = update.from(domainClass);

        update.set(DELETED_FIELD, dateTime);

        ByIdSpecification<T, ID> specification = new ByIdSpecification<>(entityInformation, (ID) entityInformation.getId(entity));
        update.where(specification.toPredicate(root, null, cb));

        return em.createQuery(update).executeUpdate();
    }


    @Override
    public long countActive() {
        return super.count(notDeleted());
    }

    @Override
    public boolean existsActive(ID id) {
        Assert.notNull(id, "The entity must not be null!");
        return findOneActive(id).isPresent();
    }

    /**
     * Convert all conditions of the given entity information to Specification
     * @param <T>
     * @param <ID>
     */
    private static final class ByIdSpecification<T, ID extends Serializable> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;
        private final ID id;

        ByIdSpecification(JpaEntityInformation<T, ?> entityInformation, ID id) {
            this.entityInformation = entityInformation;
            this.id = id;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            final List<Predicate> predicates = new ArrayList<>();
            if (entityInformation.hasCompositeId()) {
                for (String s : entityInformation.getIdAttributeNames()) {
                    predicates.add(cb.equal(root.<ID>get(s), entityInformation.getCompositeIdAttributeValue(id, s)));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
            return cb.equal(root.<ID>get(entityInformation.getIdAttribute().getName()), id);
        }
    }

    /**
     * Create a where condition: IN(id1, id2, ... )
     * @param <T>
     */
    private static final class ByIdsSpecification<T> implements Specification<T> {

        private final JpaEntityInformation<T, ?> entityInformation;

        private ParameterExpression<Iterable> parameter;

        ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }

    /**
     * Create a where condition: deleted_at IS NULL OR deleted_at > now()
     * @param <T>
     * @return
     */
    private static <T> Specification<T> notDeleted() {
        return Specification.where(new DeletedIsNull<T>()).or(new DeletedTimeGreaterThanNow<>());
    }

    /**
     * Create a where condition: deleted_at IS NULL
     * @param <T>
     */
    private static final class DeletedIsNull<T> implements Specification<T> {

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.isNull(root.<Date>get(DELETED_FIELD));
        }
    }

    /**
     * Create a where condition: deleted_at > now()
     * @param <T>
     */
    private static class DeletedTimeGreaterThanNow<T> implements Specification<T> {

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.greaterThan(root.get(DELETED_FIELD), new Date());
        }
    }
}
