package vn.prostylee.comment.service;

import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

public interface CommentService extends CrudService<CommentRequest, CommentResponse, Long>, FieldValueExists, EntityExists<Long> {

}
