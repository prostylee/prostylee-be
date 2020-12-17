package vn.prostylee.comment.service;

import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.service.CrudService;

public interface CommentService extends CrudService<CommentRequest, CommentResponse, Long> {

}
