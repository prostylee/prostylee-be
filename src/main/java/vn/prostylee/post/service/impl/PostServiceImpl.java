package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.repository.PostImageRepository;
import vn.prostylee.post.repository.PostRepository;
import vn.prostylee.post.service.PostService;
import vn.prostylee.product.dto.response.BrandResponse;
import vn.prostylee.product.entity.Brand;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final BaseFilterSpecs<Post> baseFilterSpecs;

    @Override
    public Page<PostResponse> findAll(BaseFilter baseFilter) {
        PostFilter postFilter = (PostFilter) baseFilter;
        Specification<Post> searchable = baseFilterSpecs.search(postFilter);
        Pageable pageable = baseFilterSpecs.page(postFilter);
        Page<Post> page = postRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, PostResponse.class));
    }

    @Override
    public PostResponse findById(Long id) {
        return BeanUtil.copyProperties(getById(id), PostResponse.class);
    }

    @Override
    public PostResponse save(PostRequest postRequest) {
        Post entity = BeanUtil.copyProperties(postRequest, Post.class);
        Post savedEntity = postRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, PostResponse.class);
    }

    @Override
    public PostResponse update(Long id, PostRequest postRequest) {
        Post entity = getById(id);
        BeanUtil.mergeProperties(postRequest, entity);
        Post savedEntity = postRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, PostResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            Post entity = getById(id);
            if(null != entity)
                postRepository.deleteById(id);
            log.info("Product with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Product id {} does not exists", id);
            return false;
        }
    }

    private Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post is not found with id [" + id + "]"));
    }
}
