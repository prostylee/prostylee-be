package vn.prostylee.comment.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping(value = ApiVersion.API_V1 + "/comments")
public class CommentController extends CrudController<CommentRequest, CommentResponse, Long, CommentFilter> {

    private CommentService service;

    @Autowired
    public CommentController(CommentService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/stores/{storeId}")
    public Page<CommentResponse> getAllByStore(@PathVariable Long id,@RequestBody CommentFilter baseFilter) {
        return service.getCommentsByStoreId(id ,baseFilter);
    }

    @GetMapping("/products/{productId}")
    public Page<CommentResponse> getAllByProduct(@PathVariable Long id,@RequestBody CommentFilter baseFilter) {
        return service.getAllByProduct(id, baseFilter);
    }
}