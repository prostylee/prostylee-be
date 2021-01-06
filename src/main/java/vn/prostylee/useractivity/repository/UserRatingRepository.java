package vn.prostylee.useractivity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
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
}
