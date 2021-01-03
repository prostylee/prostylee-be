package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;
import vn.prostylee.useractivity.entity.UserRating;
import vn.prostylee.useractivity.repository.UserRatingRepository;
import vn.prostylee.useractivity.service.UserRatingService;

@Service
@RequiredArgsConstructor
public class UserRatingServiceImpl implements UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final BaseFilterSpecs<UserRating> baseFilterSpecs;

    @Override
    public long count(UserRatingFilter filter){
        Specification<UserRating> searchable = getUserRatingSpecification(filter);
        return userRatingRepository.count(searchable);
    }

    @Override
    public double average(UserRatingFilter filter){
        return userRatingRepository.average(filter.getTargetId(), filter.getTargetType());
    }

    @Override
    public Page<UserRatingResponse> findAll(BaseFilter baseFilter) {
        Specification<UserRating> searchable = getUserRatingSpecification((UserRatingFilter) baseFilter);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<UserRating> page = userRatingRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserRatingResponse.class));
    }

    @Override
    public UserRatingResponse findById(Long id) {
        UserRating entity = getById(id);
        return BeanUtil.copyProperties(entity, UserRatingResponse.class);
    }

    @Override
    public UserRatingResponse save(UserRatingRequest req) {
        UserRating entity =  BeanUtil.copyProperties(req, UserRating.class);
        UserRating savedEntity = userRatingRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, UserRatingResponse.class);
    }

    @Override
    public UserRatingResponse update(Long id, UserRatingRequest req) {
        UserRating entity = getById(id);
        BeanUtil.mergeProperties(req, entity);
        UserRating savedUser = userRatingRepository.save(entity);
        return BeanUtil.copyProperties(savedUser, UserRatingResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            userRatingRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
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

        if(filter.getLevel() != null){
            Specification<UserRating> targetId = (root, query, cb) ->
                    cb.equal(root.get(UserActivityConstant.LEVEL), filter.getLevel());
            searchable = searchable.and(targetId);
        }
        return searchable;
    }
}
