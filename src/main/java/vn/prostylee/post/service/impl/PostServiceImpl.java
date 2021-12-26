package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostForListResponse;
import vn.prostylee.post.dto.response.PostForUpdateResponse;
import vn.prostylee.post.dto.response.PostImageResponse;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;
import vn.prostylee.post.repository.PostRepository;
import vn.prostylee.post.service.PostImageService;
import vn.prostylee.post.service.PostService;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.story.dto.response.UserResponseLite;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final String CREATED_BY = "createdBy";
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final BaseFilterSpecs<Post> baseFilterSpecs;
    private final FileUploadService fileUploadService;
    private final StoreService storeService;
    private final UserProfileService userProfileService;

    @Override
    public Page<PostResponse> findAll(BaseFilter baseFilter) {
        PostFilter postFilter = (PostFilter) baseFilter;
        Specification<Post> searchable = baseFilterSpecs.search(postFilter);
        if (postFilter.getUserId() != null) {
            Specification<Post> additionalSpec = (root, query, cb) -> cb.equal(root.get(CREATED_BY), postFilter.getUserId());
            searchable = searchable.and(additionalSpec);
        }
        Pageable pageable = baseFilterSpecs.page(postFilter);
        Page<Post> page = postRepository.findAll(searchable, pageable);
        return page.map(this::toListResponse);
    }

    @Override
    public PostForUpdateResponse findById(Long id) {
        return toUpdateResponse(getById(id));
    }

    @Override
    public PostResponse save(PostRequest postRequest) {
        Post entity = BeanUtil.copyProperties(postRequest, Post.class);
        List<MediaRequest> newImageRequests = postRequest.getImages();
        if (CollectionUtils.isNotEmpty(newImageRequests)) {
            Set<PostImage> savedImages = postImageService.saveImages(newImageRequests, entity);
            entity.setPostImages(savedImages);
        }
        Post savedEntity = postRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, PostResponse.class);
    }

    @Override
    public PostForUpdateResponse update(Long id, PostRequest postRequest) {
        Post entity = getById(id);
        BeanUtil.mergeProperties(postRequest, entity);
        removeImages(postRequest.getAttachmentDeleteIds(), entity);
        addNewImages(postRequest, entity);
        Post savedEntity = postRepository.save(entity);
        return toUpdateResponse(savedEntity);
    }

    private void removeImages(List<Long> deleteIds, Post entity) {
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            Set<PostImage> deleteSets = getPostImagesNeedToDelete(entity, deleteIds);
            entity.getPostImages().removeIf(deleteSets::contains);
        }
    }

    private void addNewImages(PostRequest postRequest, Post entity) {
        List<MediaRequest> newImageRequests = postRequest.getImages();
        if (CollectionUtils.isNotEmpty(newImageRequests)) {
            Set<PostImage> savedImages = postImageService.saveImages(newImageRequests, entity);
            entity.getPostImages().addAll(savedImages);
        }
    }

    private Set<PostImage> getPostImagesNeedToDelete(Post entity, List<Long> attachmentIds) {
        return entity.getPostImages().stream()
                .filter(image -> attachmentIds.contains(image.getAttachmentId()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            postRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Post id [{}] does not exists", id);
            return false;
        }
    }

    private Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post is not found with id [" + id + "]"));
    }

    private PostForListResponse toListResponse(Post post) {
        PostForListResponse postForListResponse = BeanUtil.copyProperties(post, PostForListResponse.class);
        Set<PostImage> postImages = post.getPostImages();
        List<Long> attachmentIds = postImages.stream().map(PostImage::getAttachmentId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            List<String> imageUrls = fileUploadService.getImageUrls(attachmentIds, ImageSize.POST_SIZE.getWidth(), ImageSize.POST_SIZE.getHeight());
            postForListResponse.setImageUrls(imageUrls);
        }

        Long storeId = post.getStoreId();
        if (null != storeId) {
            postForListResponse.setStoreResponseLite(storeService.getStoreResponseLite(storeId));
        }
        if (null != post.getCreatedBy()) {
            postForListResponse.setUserResponseLite(this.getUserResponseLite(post.getCreatedBy()));
        }

        return postForListResponse;
    }

    private UserResponseLite getUserResponseLite(Long id) {
        try {
            UserResponse profileBy = userProfileService.getProfileBy(id);
            return BeanUtil.copyProperties(profileBy, UserResponseLite.class);
        } catch (ResourceNotFoundException e) {
            log.warn("Profile not found with id={}", id, e);
            return null;
        }
    }

    private PostForUpdateResponse toUpdateResponse(Post post) {
        PostForUpdateResponse response = BeanUtil.copyProperties(post, PostForUpdateResponse.class);

        Optional.ofNullable(post.getStoreId())
                .ifPresent(storeId -> response.setStoreResponseLite(storeService.getStoreResponseLite(storeId)));

        Optional.ofNullable(response.getPostImages())
                .orElseGet(Collections::emptyList)
                .stream()
                .sorted(Comparator.comparingInt(PostImageResponse::getOrder).thenComparing(PostImageResponse::getId))
                .forEach(dto -> dto.setUrl(fileUploadService.getImageUrl(dto.getAttachmentId(), ImageSize.POST_SIZE.getWidth(), ImageSize.POST_SIZE.getHeight())));

        return response;
    }

    @Override
    public Page<PostResponse> getNewFeeds(PostFilter postFilter) {
        return findAll(postFilter); // TODO get new feeds
    }

    @Override
    public long countTotalPostsByStoreId(Long storeId) {
        return postRepository.countPostsByStoreId(storeId);
    }
}
