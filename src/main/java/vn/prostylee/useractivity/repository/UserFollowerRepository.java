package vn.prostylee.useractivity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.entity.UserFollower;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Repository for domain model class User Follower.
 * @see UserFollower;
 * @author prostylee
 */
@Repository
public interface UserFollowerRepository extends BaseRepository<UserFollower, Long> {

    @Modifying
    @Query("DELETE FROM UserFollower WHERE targetId=:targetId AND targetType=:targetType AND createdBy=:createdBy")
    void unfollow(
            @Param("targetId") Long targetId,
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );

    @Query("SELECT e.targetId FROM UserFollower e WHERE targetId IN :targetIds AND targetType=:targetType AND createdBy=:createdBy")
    List<Long> loadStatusFollows(
            @Param("targetIds") List<Long> targetIds,
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );
}
