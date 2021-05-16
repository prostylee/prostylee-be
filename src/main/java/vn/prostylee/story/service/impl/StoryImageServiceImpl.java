package vn.prostylee.story.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.story.entity.Story;
import vn.prostylee.story.entity.StoryImage;
import vn.prostylee.story.repository.StoryImageRepository;
import vn.prostylee.story.service.StoryImageService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryImageServiceImpl implements StoryImageService {

    private final StoryImageRepository repository;

    private final AttachmentService attachmentService;

    @Override
    public Set<StoryImage> getStoryImagesByStoryId(Long id) {
        return repository.getStoryImagesByStoryId(id);
    }

    @Override
    public Set<StoryImage> saveImages(List<MediaRequest> images, Story entity) {
        return attachmentService.saveAll(images)
                .stream()
                .map(attachment -> buildStoryImage(entity, attachment.getId()))
                .collect(Collectors.toSet());
    }

    private StoryImage buildStoryImage(Story entity, Long id) {
        return StoryImage.builder()
                .attachmentId(id)
                .story(entity)
                .order(0)
                .build();
    }

}
