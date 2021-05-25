package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.story.dto.response.UserResponseLite;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.dto.response.UserRatingImageResponse;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;
import vn.prostylee.useractivity.entity.UserRating;
import vn.prostylee.useractivity.entity.UserRatingAttachment;
import vn.prostylee.useractivity.repository.UserRatingRepository;
import vn.prostylee.useractivity.service.UserRatingService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRatingServiceImpl implements UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final BaseFilterSpecs<UserRating> baseFilterSpecs;
    private final AttachmentService attachmentService;
    private final FileUploadService fileUploadService;
    private final UserProfileService userProfileService;

    @Override
    public long count(UserRatingFilter filter){
        Specification<UserRating> searchable = getUserRatingSpecification(filter);
        return userRatingRepository.count(searchable);
    }

    @Override
    public double average(UserRatingFilter filter) {
        Double result = userRatingRepository.average(filter.getTargetId(), filter.getTargetType());
        return result == null ? 0d : result;
    }

    @Override
    public Page<UserRatingResponse> findAll(BaseFilter baseFilter) {
        Specification<UserRating> searchable = getUserRatingSpecification((UserRatingFilter) baseFilter);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<UserRating> page = userRatingRepository.findAll(searchable, pageable);
        return page.map(this::toResponse);
    }

    private UserRatingResponse toResponse(UserRating entity) {
        UserRatingResponse response = BeanUtil.copyProperties(entity, UserRatingResponse.class);

        List<UserRatingImageResponse> images = Optional.ofNullable(entity.getAttachments())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(this::toImage)
                .collect(Collectors.toList());
        response.setImages(images);

        response.setUser(getUser(entity.getCreatedBy()));
        return response;
    }

    private UserResponseLite getUser(Long id) {
        UserResponse profileBy = userProfileService.getProfileBy(id);
        return BeanUtil.copyProperties(profileBy, UserResponseLite.class);
    }

    private UserRatingImageResponse toImage(UserRatingAttachment attachment) {
        String thumbnail = fileUploadService.getImageUrl(attachment.getAttachmentId(), ImageSize.MEDIUM.getWidth(), ImageSize.MEDIUM.getHeight());
        String original = fileUploadService.getImageUrl(attachment.getAttachmentId(), ImageSize.FULL.getWidth(), ImageSize.FULL.getHeight());
        return UserRatingImageResponse.builder()
                .id(attachment.getId())
                .thumbnail(thumbnail)
                .original(original)
                .build();
    }

    @Override
    public UserRatingResponse findById(Long id) {
        UserRating entity = getById(id);
        return toResponse(entity);
    }

    @Override
    public UserRatingResponse save(UserRatingRequest req) {
        UserRating entity =  BeanUtil.copyProperties(req, UserRating.class);

        Set<UserRatingAttachment> attachments = attachmentService.saveAll(req.getImages())
                .stream()
                .map(Attachment::getId)
                .map(attachmentId -> UserRatingAttachment.builder().userRating(entity).attachmentId(attachmentId).build())
                .collect(Collectors.toSet());
        entity.setAttachments(attachments);

        UserRating savedEntity = userRatingRepository.save(entity);
        return toResponse(savedEntity);
    }

    @Override
    public UserRatingResponse update(Long id, UserRatingRequest req) {
        UserRating entity = getById(id);
        BeanUtil.mergeProperties(req, entity);
        UserRating savedEntity = userRatingRepository.save(entity);
        return toResponse(savedEntity);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            userRatingRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    private UserRating getById(Long id) {
        return userRatingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserRating is not found with id [" + id + "]"));
    }

    private Specification<UserRating> getUserRatingSpecification(UserRatingFilter filter) {
        Specification<UserRating> searchable = baseFilterSpecs.search(filter);
        if(filter.getTargetType() != null) {
            Specification<UserRating> targetType = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.TARGET_TYPE), filter.getTargetType());
            searchable = searchable.and(targetType);
        }

        if(filter.getTargetId() != null){
            Specification<UserRating> targetId = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.TARGET_ID), filter.getTargetId());
            searchable = searchable.and(targetId);
        }

        if(filter.getValue() != null){
            Specification<UserRating> targetId = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.VALUE), filter.getValue());
            searchable = searchable.and(targetId);
        }
        return searchable;
    }

    @Override
    public Page<RatingResultCountResponse> countRatingResult(PagingParam pagingParam) {
        Pageable pageSpecification = PageRequest.of(pagingParam.getPage(), pagingParam.getLimit());
        return userRatingRepository.countRatingResult(pageSpecification, TargetType.PRODUCT.toString());
    }

    @Override
    public Page<ReviewCountResponse> countNumberReview(PagingParam pagingParam){
        Pageable pageSpecification = PageRequest.of(pagingParam.getPage(), pagingParam.getLimit());
        return userRatingRepository.countNumberReview(pageSpecification, TargetType.PRODUCT.toString());
    }
}