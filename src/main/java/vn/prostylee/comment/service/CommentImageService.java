package vn.prostylee.comment.service;

import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.core.service.CrudService;

import java.util.List;

public interface CommentImageService extends CrudService<CommentImage, CommentImage, Long> {
    int saveAll(List<CommentImage> images);
}
