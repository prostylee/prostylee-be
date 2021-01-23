package vn.prostylee.story.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.constant.CommentDestinationType;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.story.constant.StoryDestinationType;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoryResponse;
import vn.prostylee.story.entity.Story;
import vn.prostylee.story.entity.StoryImage;
import vn.prostylee.story.repository.StoryImageRepository;
import vn.prostylee.story.repository.StoryRepository;
import vn.prostylee.story.service.StoryService;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    private static final String TARGET_TYPE = "targetType";
    private final StoryRepository storyRepository;
    private final BaseFilterSpecs<Story> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;

    //Get story cua chinh no
    @Override
    public Page<StoryResponse> findAll(BaseFilter baseFilter) {
        return getStoryResponses(baseFilter, null);
    }

    //Get user story mà nó follow
    @Override
    public Page<StoryResponse> getUserStoriesByUserId(BaseFilter baseFilter) {
        // lay list follow (follow user) của thang login .
        // Check xem list nay co thang nao có story ko
        // neu co add vao list story trả về

        Long id = authenticatedProvider.getUserIdValue();
        //Get List follower by createdBy
        //Because when you following someone you have  a record in DB with createdBy you.
        UserFollowerFilter userFilter = UserFollowerFilter.builder()
                .userId(id)
                .targetType("user")
                .build();
        List<Long> idFollows = userFollowerService.findAll(userFilter)
                .map(UserFollowerResponse::getTargetId)
                .stream()
                .collect(Collectors.toList());

        //
        Page<StoryResponse> storyResponses = getStoryResponses(baseFilter, StoryDestinationType.USER);
        return storyResponses;
    }

    //Get store story mà nó follow
    @Override
    public Page<StoryResponse> getStoreStoriesByUserId(BaseFilter baseFilter) {
        return getStoryResponses(baseFilter , StoryDestinationType.STORE);
    }


    private Page<StoryResponse> getStoryResponses(BaseFilter baseFilter, StoryDestinationType type) {
        Specification<Story> searchable = baseFilterSpecs.search(baseFilter);
        Specification<Story> additionalSpec = (root, query, cb) -> cb.equal(root.get(TARGET_TYPE), type.getType());
        Specification<Story> idComment = (root, query, cb) -> cb.equal(root.get("id"), authenticatedProvider.getUserIdValue());
        searchable = searchable.and(additionalSpec).and(idComment);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Story> page = storyRepository.findAllActive(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, StoryResponse.class));
    }
    @Override
    public StoryResponse findById(Long id) {
        Story story = getById(id);
        return BeanUtil.copyProperties(story, StoryResponse.class);
    }

    @Override
    public StoryResponse save(StoryRequest req) {
        Story entity = BeanUtil.copyProperties(req, Story.class);
        if (CollectionUtils.isNotEmpty(req.getAttachmentIds()))
            entity.setStoryImages(buildStoryImages(req.getAttachmentIds(), entity));
        Story savedEntity = storyRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, StoryResponse.class);
    }

    @Override
    public StoryResponse update(Long id, StoryRequest req) {
        Story entity = getById(id);
        Optional<List<Long>> attachmentIds = Optional.ofNullable(req.getAttachmentIds());
        if (attachmentIds.isPresent()) {
            processHasAttachments(entity, attachmentIds.get());
        } else {
            entity.getStoryImages().clear();
        }

        BeanUtil.mergeProperties(req, entity);
        Story savedUser = storyRepository.save(entity);
        return BeanUtil.copyProperties(savedUser, StoryResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            storyRepository.softDelete(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Story getById(Long id) {
        return storyRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story is not found with id [" + id + "]"));
    }

    private void processHasAttachments(Story entity, List<Long> attachmentIds) {
        Set<StoryImage> keepSets = getStoryImagesNeedToKeep(entity, attachmentIds);
        entity.getStoryImages().removeIf(ci -> !keepSets.contains(ci));
        Set<StoryImage> requestSets = buildImagesNeedToStore(entity, attachmentIds, keepSets);
        entity.getStoryImages().addAll(requestSets);
    }

    private Set<StoryImage> buildImagesNeedToStore(Story entity, List<Long> attachmentIds, Set<StoryImage> keepSets) {
        List<Long> keepIds = keepSets.stream().map(StoryImage::getAttachmentId).collect(Collectors.toList());
        return buildStoryImages(attachmentIds, entity).stream()
                .filter(commentImage -> !keepIds.contains(commentImage.getAttachmentId()))
                .collect(Collectors.toSet());
    }

    private Set<StoryImage> getStoryImagesNeedToKeep(Story entity, List<Long> attachmentIds) {
        return entity.getStoryImages().stream()
                .filter(commentImage -> attachmentIds.contains(commentImage.getAttachmentId()))
                .collect(Collectors.toSet());
    }

    private Set<StoryImage> buildStoryImages(List<Long> attachIds, Story entity) {
        return IntStream.range(0, attachIds.size())
                .mapToObj(index -> buildStoryImage(entity, attachIds.get(index), index + 1))
                .collect(Collectors.toSet());
    }

    private StoryImage buildStoryImage(Story entity, Long id, Integer index) {
        return StoryImage.builder().attachmentId(id)
                .story(entity).order(index)
                .build();
    }

}
