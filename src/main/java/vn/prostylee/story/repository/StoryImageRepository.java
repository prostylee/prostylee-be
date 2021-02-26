package vn.prostylee.story.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.story.entity.StoryImage;

import java.util.Set;

/**
 * Repository for domain model class StoryImage.
 * @see StoryImage ;
 * @author prostylee
 */
@Repository
public interface StoryImageRepository extends BaseRepository<StoryImage, Long> {
    Set<StoryImage> getStoryImagesByStoryId(Long id);

}
