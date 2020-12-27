package vn.prostylee.useractivity.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserLike;

/**
 * Repository for domain model class User Like.
 * @see UserLike ;
 * @author prostylee
 */
@Repository
public interface UserLikeRepository extends BaseRepository<UserLike, Long> {

    @Query("DELETE FROM UserLike WHERE targetId=:targetId AND targetType=:targetType AND createdBy=:createdBy")
    @Modifying
    void unlike(
            @Param("targetId") Long targetId,
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );
}

