package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.entity.UserFollower;
import vn.prostylee.useractivity.repository.UserFollowerRepository;
import vn.prostylee.useractivity.service.UserFollowerService;

@Service
@RequiredArgsConstructor
public class UserFollowerServiceImpl implements UserFollowerService {
    public static final String TARGET_ID = "target_id";
    private static final String TARGET_TYPE = "targetType";
    public static final String CREATED_BY = "createdBy";

    private final UserFollowerRepository repository;
    private final BaseFilterSpecs<UserFollower> baseFilterSpecs;

    @Override
    public long count(BaseFilter baseFilter, UserFollowerRequest request) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(baseFilter, request);
        return repository.count(searchable);
    }

    @Override
    public Page<UserFollowerResponse> findAll(UserFollowerRequest request, UserFollowerFilter baseFilter) {
        Specification<UserFollower> searchable = getUserFollowerSpecification(baseFilter, request);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
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
    public boolean unfollow(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Specification<UserFollower> getUserFollowerSpecification(BaseFilter baseFilter, UserFollowerRequest request) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(baseFilter);
        Specification<UserFollower> targetType = (root, query, cb) -> cb.equal(root.get(TARGET_TYPE), request.getTargetType());
        Specification<UserFollower> targetId = (root, query, cb) -> cb.equal(root.get(TARGET_ID), request.getTargetId());
        Specification<UserFollower> userIdParam = (root, query, cb) -> cb.equal(root.get(CREATED_BY), request.getUserId());
        return searchable.and(targetType).and(targetId).and(userIdParam);
    }
}
