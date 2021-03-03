package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostImageRequest;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostForListResponse;
import vn.prostylee.post.dto.response.PostForUpdateResponse;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;
import vn.prostylee.post.repository.PostRepository;
import vn.prostylee.post.service.PostImageService;
import vn.prostylee.post.service.PostService;
import vn.prostylee.store.service.StoreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final BaseFilterSpecs<Post> baseFilterSpecs;
    private final FileUploadService fileUploadService;
    private final StoreService storeService;

    @Override
    public Page<PostResponse> findAll(BaseFilter baseFilter) {
        PostFilter postFilter = (PostFilter) baseFilter;
        Specification<Post> searchable = baseFilterSpecs.search(postFilter);
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
        List<PostImageRequest> newImageRequests = postRequest.getPostImageRequests();
        if (CollectionUtils.isNotEmpty(newImageRequests)) {
            Set<PostImage> savedImages = postImageService.handlePostImages(newImageRequests, entity);
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
        return BeanUtil.copyProperties(savedEntity, PostForUpdateResponse.class);
    }

    private void removeImages(List<Long> deleteIds, Post entity) {
        if(CollectionUtils.isNotEmpty(deleteIds)){
            Set<PostImage> deleteSets = getPostImagesNeedToDelete(entity, deleteIds);
            entity.getPostImages().removeIf(ci -> deleteSets.contains(ci));
        }
    }

    private void addNewImages(PostRequest postRequest, Post entity) {
        List<PostImageRequest> newImageRequests = postRequest.getPostImageRequests();
        if (CollectionUtils.isNotEmpty(newImageRequests)) {
            Set<PostImage> savedImages = postImageService.handlePostImages(newImageRequests, entity);
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
            log.info("Post with id [{}] deleted successfully", id);
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

    private PostForListResponse toListResponse(Post post){
        PostForListResponse postForListResponse = BeanUtil.copyProperties(post, PostForListResponse.class);
        Set<PostImage> postImages = post.getPostImages();
        List<Long> attachmentIds = postImages.stream().map(PostImage::getAttachmentId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            List<String> imageUrls = fileUploadService.getImageUrls(attachmentIds, ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
            postForListResponse.setImageUrls(imageUrls);
        }

        Long storeId = post.getStoreId();
        if(null != storeId)
            postForListResponse.setStoreResponseLite(storeService.getStoreResponseLite(storeId));

        return postForListResponse;
    }

    private PostForUpdateResponse toUpdateResponse(Post post){
        PostForUpdateResponse response = BeanUtil.copyProperties(post, PostForUpdateResponse.class);
        Long storeId = post.getStoreId();
        if(null != storeId)
            response.setStoreResponseLite(storeService.getStoreResponseLite(storeId));
        response.getPostImages().forEach(dto -> dto.setUrl(fileUploadService.getImageUrl(dto.getAttachmentId(), ImageSize.SMALL.getWidth(), ImageSize.SMALL.getHeight())));
        return response;
    }
}
