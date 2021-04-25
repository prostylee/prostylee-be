package vn.prostylee.useractivity.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserTracking;

import java.util.List;

/**
 * Repository for domain model class UserTracking.
 * @see UserTracking ;
 * @author prostylee
 */
@Repository
public interface UserTrackingRepository extends BaseRepository<UserTracking, Long> {

    @Query(value = "SELECT distinct * " +
            " FROM (SELECT product_id " +
            "   FROM user_tracking e " +
            "   WHERE product_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getProductIds(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT distinct * " +
            " FROM (SELECT store_id " +
            "   FROM user_tracking e " +
            "   WHERE store_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getStoreIds(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT distinct * " +
            " FROM (SELECT category_id " +
            "   FROM user_tracking e " +
            "   WHERE category_id IS NOT NULL AND e.created_by = :userId " +
            "   ORDER BY e.created_at DESC) AS tmp", nativeQuery = true)
    List<Long> getCategoryIds(@Param("userId") Long userId, Pageable pageable);

}
