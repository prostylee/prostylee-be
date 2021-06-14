package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.useractivity.constant.UserActivityConstant;
import vn.prostylee.useractivity.dto.filter.UserWishListFilter;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
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
    private final ProductService productService;

    @Override
    public boolean addToWishList(UserWishListRequest request) {
        try {
            Long productId = request.getProductId();
            Long userIdValue = authenticatedProvider.getUserIdValue();
            UserWishList entity = repository.findByProductIdAndCreatedBy(productId, userIdValue);

            if(entity != null && entity.getDeletedAt() == null){
                return false;
            } else if(entity != null && entity.getDeletedAt() != null){
                entity.setDeletedAt(null);
                repository.save(entity);
                return true;
            }

            UserWishList copyEntity = BeanUtil.copyProperties(request, UserWishList.class);
            repository.save(copyEntity);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean removeFromWishList(Long id) {
        try {
            repository.softDelete(id);
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
                .stream().map(response -> convertToResponse(response))
                .collect(Collectors.toList());
        return new PageImpl<>(responses,pageable,responses.size());
    }

    @Override
    public UserWishListResponse loadStatusFollows(Long productId) {
        UserWishList userWishList = repository.findByProductIdAndCreatedBy(productId, authenticatedProvider.getUserIdValue());
        if(userWishList != null && userWishList.getDeletedAt() == null){
            return BeanUtil.copyProperties(userWishList, UserWishListResponse.class);
        }
        return null;
    }

    private Specification<UserWishList> getUserWishListSpecification(UserWishListFilter filter) {
        Specification<UserWishList> searchable = baseFilterSpecs.search(filter);
        Specification<UserWishList> userIdParam = (root, query, cb) ->
                cb.equal(root.get(UserActivityConstant.CREATED_BY), authenticatedProvider.getUserIdValue());
        return searchable.and(userIdParam);
    }

    private UserWishListResponse convertToResponse(UserWishList userWishList) {
        UserWishListResponse userWishListResponse = BeanUtil.copyProperties(userWishList, UserWishListResponse.class);
        ProductResponse productResponse = productService.findById(userWishList.getId());
        ProductResponseLite productLite = BeanUtil.copyProperties(productResponse, ProductResponseLite.class);
        productLite.setImageUrl(productResponse.getImageUrls().get(0));
        userWishListResponse.setProductResponseLite(productLite);
        return userWishListResponse;
    }
}
