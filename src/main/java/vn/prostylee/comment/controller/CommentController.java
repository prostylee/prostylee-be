package vn.prostylee.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.comment.dto.filter.CommentFilter;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.service.CommentService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/comments")
public class CommentController extends CrudController<CommentRequest, CommentResponse, Long, CommentFilter> {

    private final CommentService service;

    @Autowired
    public CommentController(CommentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/stores/{storeId}")
    public Page<CommentResponse> getAllByStore(@PathVariable Long storeId, @RequestBody CommentFilter baseFilter) {
        return service.getCommentsByStoreId(storeId ,baseFilter);
    }

    @GetMapping("/products/{productId}")
    public Page<CommentResponse> getAllByProduct(@PathVariable Long productId, @RequestBody CommentFilter baseFilter) {
        return service.getAllByProduct(productId, baseFilter);
    }
}
