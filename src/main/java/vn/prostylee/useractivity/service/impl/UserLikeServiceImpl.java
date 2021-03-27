package vn.prostylee.useractivity.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.request.StatusLikeRequest;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;
import vn.prostylee.useractivity.entity.UserLike;
import vn.prostylee.useractivity.repository.UserLikeRepository;
import vn.prostylee.useractivity.service.UserLikeService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserLikeServiceImpl implements UserLikeService {
    private final UserLikeRepository repository;
    private final BaseFilterSpecs<UserLike> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public long count(UserLikeFilter filter) {
        Specification<UserLike> searchable = getUserLikeSpecification(filter);
        return repository.count(searchable);
    }

    @Override
    public Page<UserLikeResponse> findAll(UserLikeFilter filter) {
        Specification<UserLike> searchable = getUserLikeSpecification(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<UserLike> page = repository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserLikeResponse.class));
    }

    @Override
    public boolean like(UserLikeRequest request) {
        try {
            UserLike entity = BeanUtil.copyProperties(request, UserLike.class);
            if(!repository.existsByTargetIdAndTargetType(request.getTargetId(), request.getTargetType()))
                repository.save(entity);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean unlike(UserLikeRequest request) {
        try {
            repository.unlike(request.getTargetId(), request.getTargetType(), authenticatedProvider.getUserIdValue());
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public List<Long> loadStatusLikes(StatusLikeRequest request) {
        return repository.loadStatusLikes(request.getTargetIds(), request.getTargetType(), authenticatedProvider.getUserIdValue());
    }

    @Override
    public List<Long> getTopBeLikes(MostActiveRequest request) {
        Pageable pageSpecification = PageRequest.of(request.getPage(), request.getLimit());
        return repository.getTopBeLikes(request.getTargetTypes(),
                request.getCustomFieldId1(), request.getCustomFieldId2(), request.getCustomFieldId3(),
                request.getFromDate(), request.getToDate(), pageSpecification);
    }

    private Specification<UserLike> getUserLikeSpecification(UserLikeFilter filter) {
        Specification<UserLike> searchable = baseFilterSpecs.search(filter);

        if(filter.getTargetType() != null) {
            Specification<UserLike> targetType = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.TARGET_TYPE), filter.getTargetType());
            searchable = searchable.and(targetType);
        }

        if(filter.getTargetId() != null){
            Specification<UserLike> targetId = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.TARGET_ID), filter.getTargetId());
            searchable = searchable.and(targetId);
        }

        if(filter.getUserId() != null ) {
            Specification<UserLike> userIdParam = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.CREATED_BY), authenticatedProvider.getUserIdValue());
            searchable = searchable.and(userIdParam);
        }
        return searchable;
    }
}
