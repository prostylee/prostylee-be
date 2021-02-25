package vn.prostylee.product.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductShippingProvider;

/**
 * Repository for domain model class Product.
 * @see ProductShippingProvider ;
 * @author prostylee
 */
@Repository
public interface ProductShippingProviderRepository extends BaseRepository<ProductShippingProvider, Long> {
}
