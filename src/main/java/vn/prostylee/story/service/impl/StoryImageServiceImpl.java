package vn.prostylee.story.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.story.entity.StoryImage;
import vn.prostylee.story.repository.StoryImageRepository;
import vn.prostylee.story.service.StoryImageService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class StoryImageServiceImpl implements StoryImageService {

    private final StoryImageRepository repository;

    @Override
    public Set<StoryImage> getStoryImagesByStoryId(Long id) {
        return repository.getStoryImagesByStoryId(id);
    }

}
