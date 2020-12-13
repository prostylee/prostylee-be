package vn.prostylee.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.comment.dto.filter.CommentFilter;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@Slf4j
@RequestMapping(value = ApiVersion.API_V1 + "/comments")
public class CommentController extends CrudController<CommentRequest, CommentResponse, Long, CommentFilter> {

    @Autowired
    public CommentController(@Qualifier("userService") UserService service) {
        super(service);
    }
}
