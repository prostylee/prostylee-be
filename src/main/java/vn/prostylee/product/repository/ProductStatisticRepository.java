package vn.prostylee.product.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductStatistic;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStatisticRepository extends BaseRepository<ProductStatistic, Long> {

    Optional<ProductStatistic> findProductStatisticByProduct(Product product);

    @Query("SELECT e FROM #{#entityName} e where e.product.id in :ids")
    List<ProductStatistic> findByProductIds(@Param("ids") Collection<Long> ids);
}
