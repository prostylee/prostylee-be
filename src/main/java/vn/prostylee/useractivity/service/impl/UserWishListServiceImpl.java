package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserWishListFilter;
import vn.prostylee.useractivity.dto.request.UserWishListRequest;
import vn.prostylee.useractivity.dto.response.UserWishListResponse;
import vn.prostylee.useractivity.entity.UserWishList;
import vn.prostylee.useractivity.repository.UserWishListRepository;
import vn.prostylee.useractivity.service.UserWishListService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWishListServiceImpl implements UserWishListService {

    private final UserWishListRepository repository;
    private final BaseFilterSpecs<UserWishList> baseFilterSpecs;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public boolean addToWishList(UserWishListRequest request) {
        try {
            UserWishList entity = BeanUtil.copyProperties(request, UserWishList.class);
            repository.save(entity);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public Page<UserWishListResponse> findAll(UserWishListFilter filter) {
        Specification<UserWishList> searchable = getUserWishListSpecification(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<UserWishList> page = repository.findAll(searchable, pageable);
        List<UserWishListResponse> responses = page.getContent()
                .stream().map(UserWishListServiceImpl::convertToResponse).collect(Collectors.toList());
        return new PageImpl<>(responses,pageable,responses.size());
    }

    private Specification<UserWishList> getUserWishListSpecification(UserWishListFilter filter) {
        Specification<UserWishList> searchable = baseFilterSpecs.search(filter);
        Specification<UserWishList> userIdParam = (root, query, cb) ->
                cb.equal(root.get(UserActivityConstant.CREATED_BY), authenticatedProvider.getUserIdValue());
        return searchable.and(userIdParam);
    }

    private static UserWishListResponse convertToResponse(UserWishList userWishList) {
        UserWishListResponse userLikeResponse = BeanUtil.copyProperties(userWishList, UserWishListResponse.class);
        return userLikeResponse;
    }
}
