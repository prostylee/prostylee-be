package vn.prostylee.comment.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.constant.CommentDestinationType;
import vn.prostylee.comment.dto.request.CommentRequest;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.comment.repository.CommentRepository;
import vn.prostylee.comment.service.CommentService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final String TARGET_TYPE = "targetType";
    private final CommentRepository commentRepo;
    private final BaseFilterSpecs<Comment> baseFilterSpecs;

    @Override
    public Page<CommentResponse> findAll(BaseFilter baseFilter) {
        Specification<Comment> searchable = baseFilterSpecs.search(baseFilter);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Comment> page = commentRepo.findAllActive(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, CommentResponse.class));
    }

    @Override
    public Page<CommentResponse> getCommentsByStoreId(Long id, BaseFilter baseFilter) {
        return getCommentResponses(id, baseFilter, CommentDestinationType.STORE);
    }

    @Override
    public Page<CommentResponse> getAllByProduct(Long id, BaseFilter baseFilter) {
        return getCommentResponses(id, baseFilter, CommentDestinationType.PRODUCT);
    }

    @Override
    public CommentResponse findById(Long id) {
        Comment comment = getById(id);
        return BeanUtil.copyProperties(comment, CommentResponse.class);
    }

    @Override
    public CommentResponse save(CommentRequest req) {
        Comment entity = BeanUtil.copyProperties(req, Comment.class);
        if (CollectionUtils.isNotEmpty(req.getAttachmentIds()))
            entity.setCommentImages(buildCommentImages(req.getAttachmentIds(), entity));
        Comment savedEntity = commentRepo.save(entity);
        return BeanUtil.copyProperties(savedEntity, CommentResponse.class);
    }

    @Override
    public CommentResponse update(Long id, CommentRequest req) {
        Comment entity = getById(id);
        Optional<List<Long>> attachmentIds = Optional.ofNullable(req.getAttachmentIds());
        if (attachmentIds.isPresent()) {
            processHasAttachments(entity, attachmentIds.get());
        } else {
            entity.getCommentImages().clear();
        }

        BeanUtil.mergeProperties(req, entity);
        Comment savedUser = commentRepo.save(entity);
        return BeanUtil.copyProperties(savedUser, CommentResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            return commentRepo.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Comment getById(Long id) {
        return commentRepo.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment is not found with id [" + id + "]"));
    }

    private Page<CommentResponse> getCommentResponses(Long id, BaseFilter baseFilter, CommentDestinationType type) {
        Specification<Comment> searchable = baseFilterSpecs.search(baseFilter);
        Specification<Comment> additionalSpec = (root, query, cb) -> cb.equal(root.get(TARGET_TYPE), type);
        Specification<Comment> idComment = (root, query, cb) -> cb.equal(root.get("id"), id);
        searchable = searchable.and(additionalSpec).and(idComment);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Comment> page = commentRepo.findAllActive(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, CommentResponse.class));
    }

    private void processHasAttachments(Comment entity, List<Long> attachmentIds) {
        Set<CommentImage> keepSets = getCommentImagesNeedToKeep(entity, attachmentIds);
        entity.getCommentImages().removeIf(ci -> !keepSets.contains(ci));
        Set<CommentImage> requestSets = buildImagesNeedToStore(entity, attachmentIds, keepSets);
        entity.getCommentImages().addAll(requestSets);
    }

    private Set<CommentImage> buildImagesNeedToStore(Comment entity, List<Long> attachmentIds, Set<CommentImage> keepSets) {
        List<Long> keepIds = keepSets.stream().map(CommentImage::getAttachmentId).collect(Collectors.toList());
        return buildCommentImages(attachmentIds, entity).stream()
                .filter(commentImage -> !keepIds.contains(commentImage.getAttachmentId()))
                .collect(Collectors.toSet());
    }

    private Set<CommentImage> getCommentImagesNeedToKeep(Comment entity, List<Long> attachmentIds) {
        return entity.getCommentImages().stream()
                .filter(commentImage -> attachmentIds.contains(commentImage.getAttachmentId()))
                .collect(Collectors.toSet());
    }

    private Set<CommentImage> buildCommentImages(List<Long> attachIds, Comment entity) {
        return IntStream.range(0, attachIds.size())
                .mapToObj(index -> buildCommentImage(entity, attachIds.get(index), index + 1))
                .collect(Collectors.toSet());
    }

    private CommentImage buildCommentImage(Comment entity, Long id, Integer index) {
        return CommentImage.builder().attachmentId(id)
                .comment(entity).order(index)
                .build();
    }

}
