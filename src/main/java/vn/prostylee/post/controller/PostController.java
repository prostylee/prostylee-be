package vn.prostylee.post.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.post.dto.filter.PostFilter;
import vn.prostylee.post.dto.request.PostRequest;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.post.service.PostService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/posts")
public class PostController extends CrudController<PostRequest, PostResponse, Long, PostFilter> {

    private final PostService postService;
    private final AuthenticatedProvider authenticatedProvider;

    public PostController(PostService postService, AuthenticatedProvider authenticatedProvider) {
        super(postService);
        this.postService = postService;
        this.authenticatedProvider = authenticatedProvider;
    }

    @GetMapping("/me")
    public Page<PostResponse> getPostsByMe(@Valid PostFilter postFilter) {
        postFilter.setUserId(authenticatedProvider.getUserIdValue());
        return postService.findAll(postFilter);
    }

    @GetMapping("/new-feeds")
    public Page<PostResponse> getNewFeeds(@Valid PostFilter postFilter) {
        return postService.getNewFeeds(postFilter);
    }
}
