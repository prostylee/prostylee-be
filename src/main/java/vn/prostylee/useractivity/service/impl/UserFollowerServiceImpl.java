package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.filter.UserFollowerPageable;
import vn.prostylee.useractivity.dto.filter.UserFollowingFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.entity.UserFollower;
import vn.prostylee.useractivity.repository.UserFollowerRepository;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFollowerServiceImpl implements UserFollowerService {

    private final UserFollowerRepository repository;
    private final BaseFilterSpecs<UserFollower> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;
    private StoreService storeService;
    private UserService userService;

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
        List<UserFollowerResponse> responses = page.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses,pageable,responses.size());
    }

    @Override
    public boolean follow(UserFollowerRequest request) {
        try {
            UserFollower entity = BeanUtil.copyProperties(request, UserFollower.class);
            if (!repository.existsByTargetIdAndTargetType(request.getTargetId(), request.getTargetType()))
                repository.save(entity);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean unfollow(UserFollowerRequest request) {
        try {
            repository.unfollow(request.getTargetId(), request.getTargetType(), authenticatedProvider.getUserIdValue());
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public List<Long> loadStatusFollows(StatusFollowRequest checkFollowRequest) {
        return repository.loadStatusFollows(checkFollowRequest.getTargetIds(), checkFollowRequest.getTargetType(), authenticatedProvider.getUserIdValue());
    }

    @Override
    public List<Long> getTopBeFollows(MostActiveRequest request) {
        Pageable pageSpecification = PageRequest.of(request.getPage(), request.getLimit());
        return repository.getTopBeFollows(request.getTargetTypes(),
                request.getCustomFieldId1(), request.getCustomFieldId2(), request.getCustomFieldId3(),
                request.getFromDate(), request.getToDate(), pageSpecification);
    }

    @Override
    public Page<Long> getFollowersByMe(UserFollowerPageable userFollowerPageable) {
        return getFollowersByUserId(authenticatedProvider.getUserIdValue(), userFollowerPageable);
    }

    @Override
    public Page<Long> getFollowersByUserId(Long userId, UserFollowerPageable userFollowerPageable) {
        UserFollowerFilter userFollowerFilter = UserFollowerFilter.builder()
                .userId(userId)
                .build();
        userFollowerFilter.setKeyword(userFollowerPageable.getKeyword());
        userFollowerFilter.setLimit(userFollowerPageable.getLimit());
        userFollowerFilter.setPage(userFollowerPageable.getPage());
        userFollowerFilter.setTargetType(TargetType.USER);

        Page<UserFollowerResponse> page = findAll(userFollowerFilter);
        List<Long> ids = page.getContent()
                .stream()
                .map(UserFollowerResponse::getCreatedBy)
                .collect(Collectors.toList());
        return new PageImpl<>(ids, page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<Long> getFollowingsByMe(UserFollowingFilter filter) {
        return getFollowingsByUserId(authenticatedProvider.getUserIdValue(), filter);
    }

    @Override
    public Page<Long> getFollowingsByUserId(Long userId, UserFollowingFilter userFollowingFilter) {
        UserFollowerFilter userFollowerFilter = UserFollowerFilter.builder()
                .userId(userId)
                .build();
        userFollowerFilter.setKeyword(userFollowingFilter.getKeyword());
        userFollowerFilter.setLimit(userFollowingFilter.getLimit());
        userFollowerFilter.setPage(userFollowingFilter.getPage());
        userFollowerFilter.setTargetType(userFollowingFilter.getTargetType());

        Page<UserFollowerResponse> page = findAll(userFollowerFilter);
        List<Long> ids = page.getContent()
                .stream()
                .map(UserFollowerResponse::getTargetId)
                .collect(Collectors.toList());
        return new PageImpl<>(ids, page.getPageable(), page.getTotalElements());
    }

    private Specification<UserFollower> getUserFollowerSpecification(UserFollowerFilter filter) {
        Specification<UserFollower> searchable = baseFilterSpecs.search(filter);
        if (filter.getTargetType() != null) {
            Specification<UserFollower> targetType = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_TYPE), filter.getTargetType());
            searchable = searchable.and(targetType);
        }

        if (filter.getTargetId() != null) {
            Specification<UserFollower> targetId = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.TARGET_ID), filter.getTargetId());
            searchable = searchable.and(targetId);
        }

        if (filter.getUserId() != null) {
            Specification<UserFollower> userIdParam = (root, query, cb) -> cb.equal(root.get(UserActivityConstant.CREATED_BY), filter.getUserId());
            searchable = searchable.and(userIdParam);
        }

        return searchable;
    }

    private UserFollowerResponse convertToResponse(UserFollower userFollower){
        UserFollowerResponse userFollowerResponse = BeanUtil.copyProperties(userFollower,UserFollowerResponse.class);
        switch (userFollower.getTargetType()){
            case STORE:
                Optional.ofNullable(userFollowerResponse.getTargetId())
                        .ifPresent(targetId -> userFollowerResponse.setStore(storeService.findById(targetId)));
                break;
            case USER:
                Optional.ofNullable(userFollowerResponse.getTargetId())
                        .ifPresent(targetId -> userFollowerResponse.setUser(userService.findById(targetId)));
                break;
            default:
                break;
        }
        return userFollowerResponse;
    }

    @Autowired
    public void setInit(@Lazy StoreService storeService,
                        @Lazy UserService userService){
        this.storeService = storeService;
        this.userService = userService;
    }
}
