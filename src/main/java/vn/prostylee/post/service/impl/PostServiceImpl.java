package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;
import vn.prostylee.post.repository.PostRepository;
import vn.prostylee.post.service.PostImageService;
import vn.prostylee.post.service.PostService;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.ProductImage;
import vn.prostylee.store.service.StoreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return page.map(this::toResponse);
    }

    @Override
    public PostResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public PostResponse save(PostRequest postRequest) {
        Post entity = BeanUtil.copyProperties(postRequest, Post.class);
        Set<PostImage> savedImages = postImageService.handlePostImages(postRequest.getPostImageRequests(), entity);
            entity.setPostImages(savedImages);
        Post savedEntity = postRepository.save(entity);
        return toResponse(savedEntity);
    }

    @Override
    public PostResponse update(Long id, PostRequest postRequest) {
        Post entity = getById(id);
        BeanUtil.mergeProperties(postRequest, entity);
        Set<PostImage> savedImages = postImageService.handlePostImages(postRequest.getPostImageRequests(), entity);
            entity.setPostImages(savedImages);
        //handle keep old and add new one.
            Post savedEntity = postRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, PostResponse.class);
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

    private PostResponse toResponse(Post post){
        PostResponse postResponse = BeanUtil.copyProperties(post, PostResponse.class);
        Set<PostImage> postImages = post.getPostImages();
        List<Long> attachmentIds = postImages.stream().map(PostImage::getAttachmentId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            List<String> imageUrls = fileUploadService.getImageUrls(attachmentIds, ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
            postResponse.setImageUrls(imageUrls);
        }
        Long storeId = post.getStoreId();
        if(null != storeId)
            postResponse.setStoreResponseLite(storeService.getStoreResponseLite(storeId));

        return postResponse;
    }
}
