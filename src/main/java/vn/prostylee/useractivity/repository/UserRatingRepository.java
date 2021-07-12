package vn.prostylee.useractivity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.entity.UserRating;

import java.util.List;

/**
 * Repository for domain model class User Rating.
 * @see UserRating ;
 * @author prostylee
 */
@Repository
public interface UserRatingRepository extends BaseRepository<UserRating, Long> {
    @Query("SELECT avg(value) FROM UserRating WHERE targetId=:targetId AND targetType=:targetType")
    Double average(
            @Param("targetId") Long targetId,
            @Param("targetType") TargetType targetType
    );

    @Query(value = "SELECT ur.targetId AS productId, round(AVG(ur.value),1) AS count " +
            "   FROM UserRating ur " +
            "   WHERE ur.targetType = :targetType " +
            "   GROUP BY ur.targetId " +
            "   ORDER BY ur.targetId")
    Page<RatingResultCountResponse> countRatingResult(Pageable pageable,@Param("targetType") TargetType targetType);

    @Query(value = "SELECT ur.targetId AS productId, COUNT(ur.id) AS count" +
            " FROM UserRating ur" +
            " WHERE ur.targetType = :targetType" +
            " GROUP BY ur.targetId" +
            " ORDER BY ur.targetId")
    Page<ReviewCountResponse> countNumberReview(Pageable pageable,@Param("targetType") TargetType targetType);

    @Query("SELECT e FROM UserRating e WHERE targetId=:targetId AND targetType=:targetType AND createdBy=:userId")
    List<UserRating> getListRatingByUser(
            @Param("targetId") Long targetId,
            @Param("targetType") TargetType targetType,
            @Param("userId") Long userId
    );
}
