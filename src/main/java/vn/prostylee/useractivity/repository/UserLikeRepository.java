package vn.prostylee.useractivity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.dto.response.LikeCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.entity.UserLike;

import java.util.Date;
import java.util.List;

/**
 * Repository for domain model class User Like.
 * @see UserLike ;
 * @author prostylee
 */
@Repository
public interface UserLikeRepository extends BaseRepository<UserLike, Long> {

    @Query("DELETE FROM #{#entityName} WHERE targetId=:targetId AND targetType=:targetType AND createdBy=:createdBy")
    @Modifying
    void unlike(
            @Param("targetId") Long targetId,
            @Param("targetType") TargetType targetType,
            @Param("createdBy") Long createdBy
    );

    boolean existsByCreatedByAndTargetIdAndTargetType(Long userId, Long targetId, TargetType targetType);
    
    @Query("SELECT e.targetId FROM #{#entityName} e WHERE targetId IN :targetIds AND targetType=:targetType AND createdBy=:createdBy")
    List<Long> loadStatusLikes(
            @Param("targetIds") List<Long> targetIds,
            @Param("targetType") TargetType targetType,
            @Param("createdBy") Long createdBy
    );

    @Query("SELECT e.targetId FROM #{#entityName} e " +
            "WHERE e.targetType IN :targetTypes AND e.createdAt >= :fromDate AND e.createdAt <= :toDate " +
            "   AND (:customFieldId1 = null OR :customFieldId1 = e.customFieldId1) " +
            "   AND (:customFieldId2 = null OR :customFieldId2 = e.customFieldId2) " +
            "   AND (:customFieldId3 = null OR :customFieldId3 = e.customFieldId3) " +
            "GROUP BY e.targetId " +
            "ORDER BY count(e.targetId) DESC ")
    List<Long> getTopBeLikes(
            @Param("targetTypes") List<TargetType> targetTypes,
            @Param("customFieldId1") Long customFieldId1,
            @Param("customFieldId2") Long customFieldId2,
            @Param("customFieldId3") Long customFieldId3,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);

    @Query(value = "SELECT ur.targetId AS id, COUNT(ur.id) AS count" +
            " FROM #{#entityName} ur" +
            " WHERE ur.targetType = :targetType" +
            " GROUP BY ur.targetId" +
            " ORDER BY ur.targetId")
    Page<LikeCountResponse> countNumberLike(Pageable pageable, @Param("targetType") TargetType targetType);
    
}

