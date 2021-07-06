package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductPrice;

import java.util.List;

/**
 * Repository for domain model class ProductPrice.
 * @see ProductPrice;
 * @author prostylee
 */
@Repository
public interface ProductPriceRepository extends BaseRepository<ProductPrice, Long> {

    List<ProductPrice> findAllByProductId(Long productId);

    @Query("SELECT min(e.price) FROM #{#entityName} e ")
    Double getMinProductPrice();

    @Query("SELECT max(e.price) FROM #{#entityName} e ")
    Double getMaxProductPrice();
}
