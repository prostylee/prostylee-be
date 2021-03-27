package vn.prostylee.core.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;

import java.util.Optional;

/**
 * CRUD service
 *
 * @param <T> The request type
 * @param <R> The response type
 * @param <ID> The primary key of entity
 */
public interface CrudService<T, R, ID> {

    Page<R> findAll(BaseFilter baseFilter);

    R findById(ID id);

    R save(T t);

    R update(ID id, T s);

    boolean deleteById(ID id);

    default Optional<R> fetchById(ID id) {
        try {
            return Optional.ofNullable(findById(id));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
}
