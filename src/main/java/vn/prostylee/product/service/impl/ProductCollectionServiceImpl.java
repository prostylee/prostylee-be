package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.converter.ProductConverter;
import vn.prostylee.product.dto.filter.RelatedProductFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductExtRepository;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.CategoryRelationshipService;
import vn.prostylee.product.service.ProductCollectionService;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserMostActiveService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCollectionServiceImpl implements ProductCollectionService {

    private final BaseFilterSpecs<Product> baseFilterSpecs;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final UserMostActiveService userMostActiveService;
    private final CategoryRelationshipService categoryRelationshipService;
    private final ProductExtRepository productExtRepository;
    private final ProductConverter productConverter;

    @Override
    public Page<ProductResponse> getRelatedProducts(Long productId, RelatedProductFilter relatedProductFilter) {
        Optional<Product> optProduct = productRepository.findById(productId);
        Optional<Category> optCategory = optProduct.map(Product::getCategory);
        if (optProduct.isEmpty() || optCategory.isEmpty()) {
            return Page.empty();
        }

        Category category = optCategory.get();

        if (BooleanUtils.isTrue(relatedProductFilter.getHot())) {
            List<Long> productIds = getRelatedProductIdsByMostActive(optProduct.get(), relatedProductFilter);
            if (CollectionUtils.isNotEmpty(productIds)) {
                return getRelatedProductsByMostActive(productIds);
            }
        }
        return getRelatedProductsByNewest(productId, category.getId(), relatedProductFilter.getLimit(), relatedProductFilter.getPage());
    }

    private List<Long> getRelatedProductIdsByMostActive(Product product, RelatedProductFilter relatedProductFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.PRODUCT.name()))
                .customFieldId1(product.getCategory().getId())
                .fromDate(DateUtils.getLastDaysBefore(MostActiveUserFilter.DEFAULT_TIME_RANGE_IN_DAYS))
                .toDate(Calendar.getInstance().getTime())
                .build()
                .pagingParam(new PagingParam(relatedProductFilter.getLimit() + 1, relatedProductFilter.getPage()));

        List<Long> productIds = userMostActiveService.getTargetIdsByMostActive(request);
        productIds.remove(product.getId());
        if (productIds.size() > relatedProductFilter.getLimit()) {
            productIds = productIds.subList(0, relatedProductFilter.getLimit());
        }
        return productIds;
    }

    private Page<ProductResponse> getRelatedProductsByMostActive(List<Long> productIds) {
        List<ProductResponse> products = productRepository.findProductsByIds(productIds)
                .stream()
                .map(productConverter::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(products);
    }

    private Page<ProductResponse> getRelatedProductsByNewest(Long productId, Long categoryId, int limit, int offset) {
        Sort sort = Sort.by("createdAt");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        return productRepository.getRelatedProducts(productId, categoryId, pageable)
                .map(productConverter::toResponse);
    }

    @Override
    public Page<ProductResponse> getSuggestionProducts(Long productId, SuggestionProductFilter suggestionProductFilter) {
        if (productId != null && productId > 0) {
            Optional<Product> optProduct = productRepository.findById(productId);
            Optional<Category> optCategory = optProduct.map(Product::getCategory);
            if (optProduct.isEmpty() || optCategory.isEmpty()) {
                return Page.empty();
            }

            Pageable pageable = baseFilterSpecs.page(suggestionProductFilter);
            Specification<Product> spec = baseFilterSpecs.search(suggestionProductFilter);

            if (suggestionProductFilter.getStoreId() != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("storeId"), suggestionProductFilter.getStoreId()));
            }

            Set<Long> categoryIds = categoryRelationshipService.getCategoryIdsRelationshipWith(optCategory.get().getId());
            if (CollectionUtils.isNotEmpty(categoryIds)) {
                spec = spec.and((root, query, cb) -> {
                    Join<Product, Category> category = root.join("category");
                    CriteriaBuilder.In<Long> inClause = cb.in(category.get("id"));
                    categoryIds.forEach(inClause::value);
                    return inClause;
                });
                Page<Product> page = this.productRepository.findAllActive(spec, pageable);
                return page.map(productConverter::toResponse);
            }
        }

        // In case of not providing a productId or not found any category relationships, we will get random products.
        List<ProductResponse> products = productExtRepository.getRandomProducts(suggestionProductFilter)
                .stream()
                .map(productConverter::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(products);
    }

    @Override
    public Page<ProductResponse> getBestSellerProducts(BestSellerFilter bestSellerFilter) {
        List<Long> productIds = orderService.getBestSellerProductIds(bestSellerFilter);
        if (CollectionUtils.isNotEmpty(productIds)) {
            return new PageImpl<>(productRepository.findProductsByIds(productIds)
                    .stream().map(productConverter::toResponse)
                    .collect(Collectors.toList()));
        }
        return Page.empty();
    }
}
