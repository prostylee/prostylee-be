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
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;
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
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    public static final int FIRST_INDEX = 0;
    private static final int DEFAULT_TIME_RANGE_IN_DAYS = 90;
    private static final int NUMBER_OF_TOP_FOLLOWING = 15;
    private final StoryRepository storyRepository;
    private final BaseFilterSpecs<Story> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;
    private final UserProfileService userProfileService;
    private final StoryImageService storyImageService;
    private final StoreService storeService;
    private final FileUploadService fileUploadService;
    private final ProductService productService;

    @Override
    public Page<UserStoryResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public Page<UserStoryResponse> getUserStoriesByUserId(BaseFilter baseFilter) {
        StoryFilter filter = (StoryFilter) baseFilter;
        return getUserStoryResponses(filter, TargetType.USER);
    }

    @Override
    public Page<StoreStoryResponse> getStoreStoriesByUserId(BaseFilter baseFilter) {
        StoryFilter filter = (StoryFilter) baseFilter;
        return getStoreStoryResponses(filter, TargetType.STORE);
    }

    private Page<UserStoryResponse> getUserStoryResponses(StoryFilter filter, TargetType type) {
        Pageable pageable = baseFilterSpecs.page(filter);
        List<Long> idFollows = getFollowsBy(authenticatedProvider.getUserIdValue(), type);
        if (CollectionUtils.isEmpty(idFollows)  || idFollows.stream().count() < NUMBER_OF_TOP_FOLLOWING){
            MostActiveRequest mostActiveRequest = MostActiveRequest.builder()
                    .targetTypes(Collections.singletonList(type))
                    .fromDate(DateUtils.getLastDaysBefore(DEFAULT_TIME_RANGE_IN_DAYS))
                    .toDate(Calendar.getInstance().getTime()).build();
            mostActiveRequest.setLimit(NUMBER_OF_TOP_FOLLOWING);
            idFollows.addAll(userFollowerService.getTopBeFollows(mostActiveRequest));
        }
        idFollows.add(FIRST_INDEX, authenticatedProvider.getUserIdValue());

        Page<UserStoryResponse> responses = storyRepository.getStories(idFollows, type.name(), pageable)
                .map(entity -> BeanUtil.copyProperties(entity, UserStoryResponse.class));

        responses.getContent().forEach(this::buildAdditionalData);
        return responses;
    }

    private void buildAdditionalData(UserStoryResponse response) {
        response.setStoryLargeImageUrls(this.fetchUrls(ImageSize.STORY_LARGE, response.getId()));
        response.setStorySmallImageUrls(this.fetchUrls(ImageSize.STORY_SMALL, response.getId()));
        response.setUserForStoryResponse(this.getUserForStoryBy(response.getCreatedBy()));
        response.setStoreResponseLite(Optional.ofNullable(response.getStoreId()).map(this::getStoreForStoryBy).orElse(null));
    }

    private Page<StoreStoryResponse> getStoreStoryResponses(StoryFilter filter, TargetType type) {
        Pageable pageable = baseFilterSpecs.page(filter);
        List<Long> idFollows = getFollowsBy(authenticatedProvider.getUserIdValue(), type);
        if (CollectionUtils.isEmpty(idFollows) || idFollows.stream().count() < NUMBER_OF_TOP_FOLLOWING){
            MostActiveRequest mostActiveRequest = MostActiveRequest.builder()
                    .targetTypes(Collections.singletonList(type))
                    .fromDate(DateUtils.getLastDaysBefore(DEFAULT_TIME_RANGE_IN_DAYS))
                    .toDate(Calendar.getInstance().getTime()).build();
            mostActiveRequest.setLimit(NUMBER_OF_TOP_FOLLOWING);
            idFollows.addAll(userFollowerService.getTopBeFollows(mostActiveRequest));
        }

        Page<StoreStoryResponse> responses = storyRepository.getStories(idFollows, type.name(), pageable)
                .map(entity -> BeanUtil.copyProperties(entity, StoreStoryResponse.class));

        responses.getContent().forEach(response -> {
            response.setStoryLargeImageUrls(this.fetchUrls(ImageSize.STORY_LARGE, response.getId()));
            response.setStorySmallImageUrls(this.fetchUrls(ImageSize.STORY_SMALL, response.getId()));
            response.setStoreForStoryResponse(this.getStoreForStoryBy(response.getCreatedBy()));
            response.setProductResponseLite(Optional.ofNullable(response.getProductId())
                    .map(e -> {
                        return BeanUtil.copyProperties(productService.findById(e),ProductResponseLite.class);
                    }).orElse(null));
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

    private List<Long> getFollowsBy(Long id, TargetType typeName) {
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
        entity.setStoryImages(storyImageService.saveImages(req.getImages(), entity));
        Story savedEntity = storyRepository.save(entity);

        UserStoryResponse userStoryResponse = BeanUtil.copyProperties(savedEntity, UserStoryResponse.class);
        buildAdditionalData(userStoryResponse);
        return userStoryResponse;
    }

    @Override
    public UserStoryResponse update(Long id, StoryRequest req) {
        Story entity = getById(id);
        BeanUtil.mergeProperties(req, entity);

        // TODO update images

        Story savedUser = storyRepository.save(entity);
        return BeanUtil.copyProperties(savedUser, UserStoryResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            return storyRepository.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Story getById(Long id) {
        return storyRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story is not found with id [" + id + "]"));
    }
}