package vn.prostylee.post.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;

public interface PostService extends CrudService<PostRequest, PostResponse, Long> {
}
