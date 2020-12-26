package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.TargetType;
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

    private final UserFollowerRepository repository;
    private final BaseFilterSpecs<UserFollower> baseFilterSpecs;

    @Override
    public long count(BaseFilter baseFilter, TargetType type, Long id) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(baseFilter);
        Specification<UserFollower> targetType = (root, query, cb) -> cb.equal(root.get(TARGET_TYPE), type);
        Specification<UserFollower> targetId = (root, query, cb) -> cb.equal(root.get(TARGET_ID), id);
        searchable = searchable.and(targetType).and(targetId);
        return repository.count(searchable);
    }

    @Override
    public Page<UserFollowerResponse> findAll(BaseFilter baseFilter, TargetType type, Long id, Long userId) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(baseFilter);
        Specification<UserFollower> targetTypeParam = (root, query, cb) -> cb.equal(root.get(TARGET_TYPE), type);
        Specification<UserFollower> targetIdParam = (root, query, cb) -> cb.equal(root.get(TARGET_ID), id);
        Specification<UserFollower> userIdParam = (root, query, cb) -> cb.equal(root.get("createdBy"), userId);
        searchable = searchable.and(targetTypeParam).and(targetIdParam).and(userIdParam);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<UserFollower> page = repository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, UserFollowerResponse.class));
    }

    @Override
    public UserFollowerResponse follow(Long aLong, UserFollowerRequest request) {
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
}
