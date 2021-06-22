package vn.prostylee.useractivity.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
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
            @Param("targetType") String targetType,
            @Param("createdBy") Long createdBy
    );

    boolean existsByTargetIdAndTargetType(Long userId, String targetType);
    
    @Query("SELECT e.targetId FROM #{#entityName} e WHERE targetId IN :targetIds AND targetType=:targetType AND createdBy=:createdBy")
    List<Long> loadStatusLikes(
            @Param("targetIds") List<Long> targetIds,
            @Param("targetType") String targetType,
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
            @Param("targetTypes") List<String> targetTypes,
            @Param("customFieldId1") Long customFieldId1,
            @Param("customFieldId2") Long customFieldId2,
            @Param("customFieldId3") Long customFieldId3,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);
    
}

