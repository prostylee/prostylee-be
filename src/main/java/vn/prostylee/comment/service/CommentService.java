package vn.prostylee.comment.service;

import org.springframework.data.domain.Page;
import vn.prostylee.comment.dto.filter.CommentFilter;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.service.CrudService;

public interface CommentService extends CrudService<CommentRequest, CommentResponse, Long> {
    Page<CommentResponse> getAllByStore(BaseFilter baseFilter);

    Page<CommentResponse> getAllByProduct(BaseFilter baseFilter);
}
