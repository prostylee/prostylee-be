package vn.prostylee.product.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductStatistic;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductStatisticRepository extends BaseRepository<ProductStatistic, Long> {

    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<ProductStatistic> findByProductIds(@Param("ids") Collection<Long> ids);
}
