package vn.prostylee.story.service;

import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.story.entity.Story;
import vn.prostylee.story.entity.StoryImage;

import java.util.List;
import java.util.Set;

public interface StoryImageService {

    Set<StoryImage> getStoryImagesByStoryId(Long id);

    Set<StoryImage> saveImages(List<MediaRequest> images, Story entity);
}
