package vn.prostylee.useractivity.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserTracking;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Repository for domain model class UserTracking.
 * @see UserTracking ;
 * @author prostylee
 */
@Repository
public interface UserTrackingRepository extends BaseRepository<UserTracking, Long> {

    @Query(value = "SELECT DISTINCT * " +
            " FROM (SELECT e.product_id " +
            "   FROM user_tracking e " +
            "   WHERE e.product_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getProductIds(@Nonnull @Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT DISTINCT * " +
            " FROM (SELECT e.store_id " +
            "   FROM user_tracking e " +
            "   WHERE e.store_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getStoreIds(@Nonnull @Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT DISTINCT * " +
            " FROM (SELECT e.category_id " +
            "   FROM user_tracking e " +
            "   WHERE e.category_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getCategoryIds(@Nonnull @Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT DISTINCT * " +
            " FROM (SELECT e.search_keyword " +
            "   FROM user_tracking e " +
            "   WHERE e.search_keyword IS NOT NULL AND e.path LIKE :apiPath AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<String> getRecentKeywords(
            @Nonnull @Param("userId") Long userId,
            @Param("apiPath") String apiPath,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT * " +
            " FROM (SELECT e.search_keyword " +
            "   FROM user_tracking e " +
            "   WHERE e.search_keyword IS NOT NULL AND e.path LIKE :apiPath " +
            "       AND (:userId = 0 OR e.created_by = :userId) " +
            "   GROUP BY e.search_keyword " +
            "   ORDER BY COUNT(e.search_keyword) DESC " +
            " ) AS tmp", nativeQuery = true)
    List<String> getTopKeywords(
            @Nonnull @Param("userId") Long userId,
            @Param("apiPath") String apiPath,
            Pageable pageable);

}
