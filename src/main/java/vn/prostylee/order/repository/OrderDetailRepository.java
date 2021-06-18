package vn.prostylee.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.order.dto.response.ProductOrderResponse;
import vn.prostylee.order.dto.response.ProductSoldCountResponse;
import vn.prostylee.order.entity.OrderDetail;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetail, Long> {

    @Query("SELECT e.productId " +
            " FROM #{#entityName} e " +
            " WHERE (:storeId IS NULL OR e.storeId = :storeId) " +
            "   AND e.createdAt >= :fromDate AND e.createdAt <= :toDate " +
            "   AND e.order.status <> 90" + // OrderStatus.CANCELLED.getStatus() +
            " GROUP BY e.productId " +
            " ORDER BY count(e.productId) DESC ")
    List<Long> getBestSellerProductIds(
            @Param("storeId") Long storeId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);

    @Query("SELECT e.productId AS productId, count(e.productId) AS count " +
            " FROM #{#entityName} e " +
            " WHERE e.order.status <> 90" + // OrderStatus.CANCELLED.getStatus() +
            " GROUP BY e.productId " +
            " ORDER BY count(e.productId) DESC ")
    Page<ProductSoldCountResponse> countProductSold(Pageable pageable);

    @Query(value = "SELECT DISTINCT * " +
            " FROM (" +
            "   SELECT e.product_id AS productId " +
            "   FROM order_detail e " +
            "   WHERE e.created_by = :userId " +
            "   ORDER BY e.created_at DESC " +
            " ) tmp ", nativeQuery = true)
    Page<ProductOrderResponse> getPurchasedProductIdsByUserId(@Param("userId") Long userId, Pageable pageable);

}
