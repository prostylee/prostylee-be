package vn.prostylee.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.comment.dto.filter.CommentFilter;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.Comment;
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

    @GetMapping("/get-by-store")
    public Page<CommentResponse> getAllByStore(CommentFilter baseFilter) {
        return service.getAllByStore(baseFilter);
    }

    @GetMapping("/get-by-product")
    public Page<CommentResponse> getAllByProduct(CommentFilter baseFilter) {
        return service.getAllByProduct(baseFilter);
    }
}
