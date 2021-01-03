package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.entity.UserFollower;
import vn.prostylee.useractivity.repository.UserFollowerRepository;
import vn.prostylee.useractivity.service.UserFollowerService;

@Service
@RequiredArgsConstructor
public class UserFollowerServiceImpl implements UserFollowerService {
    private final UserFollowerRepository repository;
    private final BaseFilterSpecs<UserFollower> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public long count(UserFollowerFilter filter) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(filter);
        return repository.count(searchable);
    }

    @Override
    public Page<UserFollowerResponse> findAll(UserFollowerFilter filter) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<UserFollower> page = repository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserFollowerResponse.class));
    }

    @Override
    public UserFollowerResponse follow(UserFollowerRequest request) {
        UserFollower entity = BeanUtil.copyProperties(request, UserFollower.class);
        UserFollower savedEntity = repository.save(entity);
        return BeanUtil.copyProperties(savedEntity, UserFollowerResponse.class);
    }

    @Override
    public boolean unfollow(UserFollowerRequest request) {
        try {
            repository.unfollow(request.getTargetId(), request.getTargetType(), authenticatedProvider.getUserId().get());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Specification<UserFollower> getUserFollowerSpecification(UserFollowerFilter filter) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(filter);
        if(filter.getTargetType() != null) {
            Specification<UserFollower> targetType = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_TYPE), filter.getTargetType());
            searchable = searchable.and(targetType);
        }

        if(filter.getTargetId() != null){
            Specification<UserFollower> targetId = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_ID), filter.getTargetId());
            searchable = searchable.and(targetId);
        }

        if(filter.getUserId() != null ) {
            Specification<UserFollower> userIdParam = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.CREATED_BY), filter.getUserId());
            searchable = searchable.and(userIdParam);
        }

        return searchable;
    }
}
