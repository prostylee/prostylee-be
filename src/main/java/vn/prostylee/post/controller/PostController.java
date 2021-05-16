package vn.prostylee.post.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.service.PostService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/posts")
public class PostController extends CrudController<PostRequest, PostResponse, Long, PostFilter> {

    public PostController(PostService postService) {
        super(postService);
    }

}
