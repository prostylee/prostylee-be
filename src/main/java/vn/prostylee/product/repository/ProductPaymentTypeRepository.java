package vn.prostylee.product.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductPaymentType;

/**
 * Repository for domain model class Product.
 * @see ProductPaymentTypeRepository ;
 * @author prostylee
 */
@Repository
public interface ProductPaymentTypeRepository extends BaseRepository<ProductPaymentType, Long> {
}
