package vn.prostylee.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.comment.repository.CommentRepository;
import vn.prostylee.comment.service.CommentService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;

    @Override
    public Page<CommentResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public CommentResponse findById(Long id) {
        Comment comment = getById(id);
        return BeanUtil.copyProperties(comment, CommentResponse.class);
    }

    @Override
    public CommentResponse save(CommentRequest req) {
        Comment entity = BeanUtil.copyProperties(req, Comment.class);
        entity.setCommentImages(buildCommentImages(req, entity));
        Comment savedEntity = commentRepo.save(entity);
        return  BeanUtil.copyProperties(savedEntity, CommentResponse.class);
    }

    private Set<CommentImage> buildCommentImages(CommentRequest req, Comment entity) {
        return IntStream.range(0, req.getAttachmentId().size())
                .mapToObj(index -> buildCommentImage(entity, req.getAttachmentId().get(index), index + 1))
                .collect(Collectors.toSet());
    }

    private CommentImage buildCommentImage(Comment entity, Long id, Integer index) {
        CommentImage commentImage =  new CommentImage();
        commentImage.setAttachmentId(id);
        commentImage.setComment(entity);
        commentImage.setOrder(index);
        return commentImage;
    }

    @Override
    public CommentResponse update(Long id, CommentRequest commentRequest) {
        Comment comment = getById(id);
        Comment savedUser = commentRepo.save(comment);
        return  BeanUtil.copyProperties(savedUser, CommentResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            return commentRepo.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean isEntityExists(Long aLong, Map<String, Object> uniqueValues) {
        //TODO
        return false;
    }

    @Override
    public boolean isFieldValueExists(String fieldName, Object value) {
        //TODO
        return false;
    }

    private Comment getById(Long id) {
        return commentRepo.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment is not found with id [" + id + "]"));
    }
}
