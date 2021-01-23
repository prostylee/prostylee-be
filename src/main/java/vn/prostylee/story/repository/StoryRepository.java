package vn.prostylee.story.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.story.entity.Story;

import java.util.List;

/**
 * Repository for domain model class Story.
 * @see Story ;
 * @author prostylee
 */
@Repository
public interface StoryRepository extends BaseRepository<Story, Long> {

    @Query("SELECT targetId Story WHERE targetId IN :ids AND targetType=:targetType")
    @Modifying
    void getStoriesBy(
            @Param("targetId") List<Long> ids,
            @Param("targetType") String targetType
    );
}
