package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Product;

import org.springframework.stereotype.Repository;

/**
 * Repository for domain model class Product.
 * @see Product;
 * @author prostylee
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    long countProductsByCreatedBy(Long userId);
}
