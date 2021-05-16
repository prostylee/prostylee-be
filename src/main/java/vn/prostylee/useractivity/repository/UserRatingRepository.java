package vn.prostylee.useractivity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.entity.UserRating;

/**
 * Repository for domain model class User Rating.
 * @see UserRating ;
 * @author prostylee
 */
@Repository
public interface UserRatingRepository extends BaseRepository<UserRating, Long> {
    @Query("SELECT avg(value) FROM UserRating WHERE targetId=:targetId AND targetType=:targetType")
    double average(
            @Param("targetId") Long targetId,
            @Param("targetType") String targetType
    );

    @Query(value = "SELECT ur.target_id AS productId, round(AVG(ur.value),1) AS count " +
            "   FROM user_rating ur " +
            "   WHERE ur.target_type = :targetType " +
            "   GROUP BY ur.target_id " +
            "   ORDER BY ur.target_id", nativeQuery = true)
    Page<RatingResultCountResponse> countRatingResult(Pageable pageable,@Param("targetType") String targetType);

    @Query(value = "SELECT ur.target_id AS productId, COUNT(ur.id) AS count" +
            " FROM user_rating ur" +
            " WHERE ur.target_type = :targetType" +
            " GROUP BY ur.target_id" +
            " ORDER BY ur.target_id", nativeQuery = true)
    Page<ReviewCountResponse> countNumberReview(Pageable pageable,@Param("targetType") String targetType);
}
