package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Category;

import java.util.List;

/**
 * Repository for domain model class Category.
 * @see Category;
 * @author prostylee
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {

    List<Category> findAllByIdIn(List<Long> categoryIds);

}
