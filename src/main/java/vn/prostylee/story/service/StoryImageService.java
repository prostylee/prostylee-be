package vn.prostylee.story.service;

import vn.prostylee.story.entity.StoryImage;

import java.util.Set;

public interface StoryImageService {

    Set<StoryImage> getStoryImagesByStoryId(Long Id);
}
