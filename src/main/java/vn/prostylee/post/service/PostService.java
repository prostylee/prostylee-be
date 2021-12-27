package vn.prostylee.post.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;

public interface PostService extends CrudService<PostRequest, PostResponse, Long> {

    long countTotalPostByUser(Long userId);

    Page<PostResponse> getNewFeeds(PostFilter postFilter);

    long countTotalPostsByStoreId(Long storeId);
}
