package vn.prostylee.story.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.story.constant.StoryDestinationType;
import vn.prostylee.story.dto.filter.StoryFilter;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoreForStoryResponse;
import vn.prostylee.story.dto.response.StoreStoryResponse;
import vn.prostylee.story.dto.response.UserResponseLite;
import vn.prostylee.story.dto.response.UserStoryResponse;
import vn.prostylee.story.entity.Story;
import vn.prostylee.story.entity.StoryImage;
import vn.prostylee.story.repository.StoryRepository;
import vn.prostylee.story.service.StoryImageService;
import vn.prostylee.story.service.StoryService;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {
    public static final int FIRST_INDEX = 0;
    private final StoryRepository storyRepository;
    private final BaseFilterSpecs<Story> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;
    private final UserProfileService userProfileService;
    private final StoryImageService storyImageService;
    private final StoreService storeService;
    private final FileUploadService fileUploadService;
    @Override
    public Page<UserStoryResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public Page<UserStoryResponse> getUserStoriesByUserId(BaseFilter baseFilter) {
        StoryFilter filter = (StoryFilter) baseFilter;
        return getUserStoryResponses(filter, StoryDestinationType.USER.getType());
    }

    @Override
    public Page<StoreStoryResponse> getStoreStoriesByUserId(BaseFilter baseFilter) {
        StoryFilter filter = (StoryFilter) baseFilter;
        return getStoreStoryResponses(filter, StoryDestinationType.STORE.getType());
    }

    private Page<UserStoryResponse> getUserStoryResponses(StoryFilter filter, String type) {
        Pageable pageable = baseFilterSpecs.page(filter);
        List<Long> idFollows = getFollowsBy(authenticatedProvider.getUserIdValue(), type);

        Page<UserStoryResponse> responses = storyRepository.getStories(idFollows, type, pageable)
                .map(entity -> BeanUtil.copyProperties(entity, UserStoryResponse.class));

        responses.getContent().forEach(response -> {
            response.setStoryLargeImageUrls(this.fetchUrls(ImageSize.LARGE, response.getId()));
            response.setStorySmallImageUrls(this.fetchUrls(ImageSize.SMALL, response.getId()));
            response.setUserForStoryResponse(this.getUserForStoryBy(response.getCreatedBy()));
            response.setStoreResponseLite(Optional.ofNullable(response.getStoreId()).map(this::getStoreForStoryBy).orElse(null));
        });
        return responses;
    }

    private Page<StoreStoryResponse> getStoreStoryResponses(StoryFilter filter, String type) {
        Pageable pageable = baseFilterSpecs.page(filter);
        List<Long> idFollows = getFollowsBy(authenticatedProvider.getUserIdValue(), type);

        Page<StoreStoryResponse> responses = storyRepository.getStories(idFollows, type, pageable)
                .map(entity -> BeanUtil.copyProperties(entity, StoreStoryResponse.class));

        responses.getContent().forEach(response -> {
            response.setStoryLargeImageUrls(this.fetchUrls(ImageSize.LARGE, response.getId()));
            response.setStorySmallImageUrls(this.fetchUrls(ImageSize.SMALL, response.getId()));
            response.setStoreForStoryResponse(this.getStoreForStoryBy(response.getCreatedBy()));
        });
        return responses;
    }

    private List<String> fetchUrls(ImageSize sizeType, Long storyId) {
        Set<StoryImage> storyImages = storyImageService.getStoryImagesByStoryId(storyId);
        List<Long> attachmentIds = storyImages.stream().map(StoryImage::getAttachmentId).collect(Collectors.toList());
        return fileUploadService.getImageUrls(attachmentIds, sizeType.getWidth(), sizeType.getHeight());
    }

    private StoreForStoryResponse  getStoreForStoryBy(Long id) {
        StoreResponse storeResponse = storeService.findById(id);
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(storeResponse.getLogo()),
                ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeResponse.setLogoUrl(imageUrls.get(FIRST_INDEX));
        }
        return BeanUtil.copyProperties(storeResponse, StoreForStoryResponse.class);
    }

    private UserResponseLite getUserForStoryBy(Long id) {
        UserResponse profileBy = userProfileService.getProfileBy(id);
        return BeanUtil.copyProperties(profileBy, UserResponseLite.class);
    }

    private List<Long> getFollowsBy(Long id, String typeName) {
        UserFollowerFilter userFilter = UserFollowerFilter.builder().userId(id).targetType(typeName).build();
        return userFollowerService.findAll(userFilter)
                .map(UserFollowerResponse::getTargetId)
                .stream().collect(Collectors.toList());
    }

    @Override
    public UserStoryResponse findById(Long id) {
        Story story = this.getById(id);
        return BeanUtil.copyProperties(story, UserStoryResponse.class);
    }

    @Override
    public UserStoryResponse save(StoryRequest req) {
        Story entity = BeanUtil.copyProperties(req, Story.class);
        entity.setTargetId(authenticatedProvider.getUserIdValue());
        if (CollectionUtils.isNotEmpty(req.getAttachmentIds()))
            entity.setStoryImages(buildStoryImages(req.getAttachmentIds(), entity));
        Story savedEntity = storyRepository.save(entity);
        UserStoryResponse userStoryResponse = BeanUtil.copyProperties(savedEntity, UserStoryResponse.class);
        userStoryResponse.setStoryLargeImageUrls(this.fetchUrls(ImageSize.LARGE, authenticatedProvider.getUserIdValue()));
        userStoryResponse.setStorySmallImageUrls(this.fetchUrls(ImageSize.SMALL, authenticatedProvider.getUserIdValue()));
        userStoryResponse.setUserForStoryResponse(this.getUserForStoryBy(savedEntity.getCreatedBy()));
        userStoryResponse.setStoreResponseLite(Optional.ofNullable(savedEntity.getStoreId()).map(this::getStoreForStoryBy).orElse(null));
        return userStoryResponse;
    }

    @Override
    public UserStoryResponse update(Long id, StoryRequest req) {
        Story entity = getById(id);
        Optional<List<Long>> attachmentIds = Optional.ofNullable(req.getAttachmentIds());
        if (attachmentIds.isPresent()) {
            processHasAttachments(entity, attachmentIds.get());
        } else {
            entity.getStoryImages().clear();
        }

        BeanUtil.mergeProperties(req, entity);
        Story savedUser = storyRepository.save(entity);
        return BeanUtil.copyProperties(savedUser, UserStoryResponse.class);
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