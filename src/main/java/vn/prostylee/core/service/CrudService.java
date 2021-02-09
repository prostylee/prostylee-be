package vn.prostylee.core.service;

import vn.prostylee.core.dto.filter.BaseFilter;
import org.springframework.data.domain.Page;

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
}
