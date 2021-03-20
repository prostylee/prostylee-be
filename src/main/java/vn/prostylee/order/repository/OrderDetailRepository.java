package vn.prostylee.order.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.order.entity.OrderDetail;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetail, Long> {

    @Query("SELECT e.product.id FROM #{#entityName} e " +
            " WHERE (:storeId IS NULL OR e.store.id = :storeId) " +
            "   AND e.createdAt >= :fromDate AND e.createdAt <= :toDate " +
            "   AND e.order.status <> 90 " +
            " GROUP BY e.product.id " +
            " ORDER BY count(e.product.id) DESC ")
    List<Long> getBestSellerProductIds(
            @Param("storeId") Long storeId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);
}
