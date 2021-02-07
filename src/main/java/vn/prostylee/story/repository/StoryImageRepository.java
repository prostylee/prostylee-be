package vn.prostylee.story.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.story.dto.response.StoryImageResponse;
import vn.prostylee.story.dto.response.StoryResponse;
import vn.prostylee.story.entity.StoryImage;

import java.util.List;
import java.util.Set;

/**
 * Repository for domain model class StoryImage.
 * @see StoryImage ;
 * @author prostylee
 */
@Repository
public interface StoryImageRepository extends BaseRepository<StoryImage, Long> {
    Set<StoryImage> getStoryImagesById(Long id);

}
