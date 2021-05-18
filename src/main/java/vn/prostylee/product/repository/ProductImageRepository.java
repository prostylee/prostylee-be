package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductImage;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for domain model class ProductImage.
 * @see ProductImage;
 * @author prostylee
 */
@Repository
public interface ProductImageRepository extends BaseRepository<ProductImage, Long> {

    @Query(value = "SELECT p.attachment_id " +
            "FROM product_image p " +
            "WHERE p.product_id = :productID " +
            "ORDER BY p.order ", nativeQuery = true)
    List<Long> getAttachmentIdByProductId(@Param("productID") Long productId);
}
