package vn.prostylee.useractivity.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;
import vn.prostylee.useractivity.entity.UserLike;
import vn.prostylee.useractivity.repository.UserLikeRepository;
import vn.prostylee.useractivity.service.UserLikeService;

@Service
@AllArgsConstructor
public class UserLikeServiceImpl implements UserLikeService {
    private final UserLikeRepository repository;
    private final BaseFilterSpecs<UserLike> baseFilterSpecs;

    @Override
    public long count(UserActivityRequest request, UserLikeFilter filter) {
        Specification<UserLike> searchable = getUserLikeSpecification(filter, request);
        return repository.count(searchable);
    }

    @Override
    public Page<UserLikeResponse> findAll(UserActivityRequest request, UserLikeFilter filter) {
        Specification<UserLike> searchable = getUserLikeSpecification(filter, request);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<UserLike> page = repository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserLikeResponse.class));
    }

    @Override
    public UserLikeResponse like(UserActivityRequest request) {
        UserLike entity = BeanUtil.copyProperties(request, UserLike.class);
        UserLike savedEntity = repository.save(entity);
        return BeanUtil.copyProperties(savedEntity, UserLikeResponse.class);
    }

    @Override
    public boolean unlike(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Specification<UserLike> getUserLikeSpecification(UserLikeFilter filter, UserActivityRequest request) {
        Specification<UserLike> searchable = baseFilterSpecs.search(filter);
        Specification<UserLike> targetType =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_TYPE), request.getTargetType());
        Specification<UserLike> targetId =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_ID), request.getTargetId());
        Specification<UserLike> userIdParam =
                (root, query, cb) -> cb.equal(root.get(UserActivityConstant.CREATED_BY), request.getUserId());
        return searchable.and(targetType).and(targetId).and(userIdParam);
    }
}
