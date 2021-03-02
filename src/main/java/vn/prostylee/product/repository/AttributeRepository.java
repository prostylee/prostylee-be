package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Attribute;

import java.util.Optional;

/**
 * Repository for domain model class Attribute.
 * @see Attribute;
 * @author prostylee
 */
@Repository
public interface AttributeRepository extends BaseRepository<Attribute, Long> {

    Optional<Attribute> findByCategoryIdAndId(Long categoryId, Long id);
}
