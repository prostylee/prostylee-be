package vn.prostylee.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public interface SoftDeleteRepository<T, ID extends Serializable> {

    /**
     * Find all active entities.
     * @return
     */
    Iterable<T> findAllActive();

    /**
     * Find all active entities sorted by the given options.
     * @param sort
     * @return all active entities sorted
     */
    Iterable<T> findAllActive(Sort sort);

    /**
     * Find all active entities meeting the paging restriction provided in the {@code Pageable} object.
     * @param pageable
     * @return a {@link Page} of entities
     */
    Page<T> findAllActive(Pageable pageable);

    /**
     * Find all active entities meeting the paging restriction provided in the {@code Specification} object and {@code Pageable} object.
     * @param specs
     * @param pageable
     * @return a {@link Page} of entities
     */
    Page<T> findAllActive(Specification<T> specs, Pageable pageable);

    /**
     * Find all active entities meeting the given ids
     * @param ids
     * @return all active entities
     */
    Iterable<T> findAllActive(Iterable<ID> ids);

    /**
     * Retrieves an active entity by its id.
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found.
     */
    Optional<T> findOneActive(ID id);

    /**
     * Execute an update statement to set the deleted time at now() for the given entity id
     * @param id The id of entity will be updated, must not be {@literal null}.
     */
    @Modifying
    int softDelete(ID id);

    /**
     * Execute an update statement to set the deleted time at now() for the given entity
     * @param entity The entity will be updated, must not be {@literal null}.
     */
    @Modifying
    int softDelete(T entity);

    /**
     * Execute the update statements to set the deleted time at now() for the given entities
     * @param entities The entities will be updated, must not be {@literal null}.
     */
    @Modifying
    int softDelete(Iterable<? extends T> entities);

    /**
     * Execute the update statements to set the deleted time at now() for all active entities in database
     */
    @Modifying
    int softDeleteAll();

    /**
     * Execute an update statement to set the given deleted time for the given entity id
     * @param id The id of entity will be updated, must not be {@literal null}.
     * @param dateTime The deleted time will be set
     */
    @Modifying
    int scheduleSoftDelete(ID id, Date dateTime);

    /**
     * Execute an update statement to set the given deleted time of the given entity
     * @param entity The entity will be updated, must not be {@literal null}.
     * @param dateTime The deleted time will be set
     */
    @Modifying
    int scheduleSoftDelete(T entity, Date dateTime);

    /**
     * Count the number of active entities available.
     * @return The number of active entities available
     */
    long countActive();

    /**
     * Check whether an entity with the given id exists.
     * @param id must not be {@literal null}.
     * @return
     */
    boolean existsActive(ID id);

}