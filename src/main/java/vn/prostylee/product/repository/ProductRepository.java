package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.Product;

import java.util.Date;
import java.util.List;

/**
 * Repository for domain model class Product.
 * @see Product;
 * @author prostylee
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {

    long countProductsByCreatedBy(Long userId);

    @Query("SELECT e FROM #{#entityName} e " +
            " WHERE e.category.id = :categoryId AND e.id <> :productId ")
    Page<Product> getRelatedProducts(@Param("productId") Long productId,
                                     @Param("categoryId") Long categoryId,
                                     Pageable pageable);

    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<Product> findProductsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT e.id FROM #{#entityName} e WHERE e.storeId = :id AND e.createdAt >= :fromDate AND e.createdAt <= :toDate")
    List<Long> findNewestProductIdByIds(@Param("id") Long storeId,
                                        @Param("fromDate") Date fromDate,
                                        @Param("toDate") Date toDate,
                                        Pageable pageable);
}
