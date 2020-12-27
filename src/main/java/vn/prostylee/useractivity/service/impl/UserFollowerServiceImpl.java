package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.entity.UserFollower;
import vn.prostylee.useractivity.repository.UserFollowerRepository;
import vn.prostylee.useractivity.service.UserFollowerService;

@Service
@RequiredArgsConstructor
public class UserFollowerServiceImpl implements UserFollowerService {
    private final UserFollowerRepository repository;
    private final BaseFilterSpecs<UserFollower> baseFilterSpecs;

    @Override
    public long count(UserActivityRequest request, UserFollowerFilter filter) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(filter, request);
        return repository.count(searchable);
    }

    @Override
    public Page<UserFollowerResponse> findAll(UserActivityRequest request, UserFollowerFilter filter) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(filter, request);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<UserFollower> page = repository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserFollowerResponse.class));
    }

    @Override
    public UserFollowerResponse follow(UserActivityRequest request) {
        UserFollower entity = BeanUtil.copyProperties(request, UserFollower.class);
        UserFollower savedEntity = repository.save(entity);
        return BeanUtil.copyProperties(savedEntity, UserFollowerResponse.class);
    }

    @Override
    public boolean unfollow(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Specification<UserFollower> getUserFollowerSpecification(UserFollowerFilter filter, UserActivityRequest request) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(filter);
        Specification<UserFollower> targetType =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_TYPE), request.getTargetType());
        Specification<UserFollower> targetId =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_ID), request.getTargetId());
        Specification<UserFollower> userIdParam =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.CREATED_BY), request.getUserId());
        return searchable.and(targetType).and(targetId).and(userIdParam);
    }
}
