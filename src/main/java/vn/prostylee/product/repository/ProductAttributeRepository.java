package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;

import java.util.List;

/**
 * Repository for domain model class ProductAttribute.
 * @see ProductAttribute;
 * @author prostylee
 */
@Repository
public interface ProductAttributeRepository extends BaseRepository<ProductAttribute, Long> {

    List<ProductAttribute> findAllByProduct(Product product);

    List<ProductAttribute> findAllByProductPrice(ProductPrice productPrice);

    @Query("SELECT e FROM #{#entityName} e WHERE id IN :ids")
    List<ProductAttribute> findByIdIn(@Param("ids") List<Long> ids);
}
