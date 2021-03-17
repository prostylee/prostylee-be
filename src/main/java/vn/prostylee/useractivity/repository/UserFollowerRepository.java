package vn.prostylee.useractivity.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserFollower;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Repository for domain model class User Follower.
 * @see UserFollower;
 * @author prostylee
 */
@Repository
@Transactional
public interface UserFollowerRepository extends BaseRepository<UserFollower, Long> {

    @Modifying
    @Query("DELETE FROM #{#entityName} WHERE targetId=:targetId AND targetType=:targetType AND createdBy=:createdBy")
    void unfollow(
            @Param("targetId") Long targetId,
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );

    boolean existsByTargetIdAndTargetType(Long userId, String targetType);

    @Query("SELECT e.targetId FROM #{#entityName} e WHERE targetId IN :targetIds AND targetType=:targetType AND createdBy=:createdBy")
    List<Long> loadStatusFollows(
            @Param("targetIds") List<Long> targetIds,
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );

    @Query("SELECT e.targetId FROM #{#entityName} e " +
            "WHERE e.targetType IN :targetTypes AND e.createdAt >= :fromDate AND e.createdAt <= :toDate " +
            "GROUP BY e.targetId " +
            "ORDER BY count(e.targetId) DESC ")
    List<Long> getTopBeLikes(
            @Param("targetTypes") List<String> targetTypes,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);
}
