package vn.prostylee.product.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.query.HibernateQueryResult;
import vn.prostylee.core.repository.query.NativeQueryResult;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.ProductIdFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.dto.response.NewFeedResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductExtRepository;
import vn.prostylee.product.repository.specification.NewFeedsSpecificationBuilder;
import vn.prostylee.product.repository.specification.ProductSpecificationBuilder;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ProductExtRepositoryImpl implements ProductExtRepository {

    private final EntityManager em;
    private final ProductSpecificationBuilder productSpecificationBuilder;
    private final NewFeedsSpecificationBuilder newFeedsSpecificationBuilder;

    @Override
    public Page<Product> findAll(ProductFilter productFilter) {
        Pageable pageable = PageRequest.of(productFilter.getPage(), productFilter.getLimit());
        NativeQueryResult<Product> nativeQueryResult = new NativeQueryResult<>(
                em, Product.class, productSpecificationBuilder.buildQuery(productFilter), pageable);
        return nativeQueryResult.getResultList(productSpecificationBuilder.buildParams(productFilter));
    }

    @Override
    public List<Product> getRandomProducts(SuggestionProductFilter suggestionProductFilter) {
        StringBuilder sb = new StringBuilder()
                .append(" SELECT e")
                .append(" FROM Product e")
                .append(" WHERE 1 = 1");
        if (StringUtils.isNotBlank(suggestionProductFilter.getKeyword())) {
            sb.append(" AND LOWER(e.name) LIKE :keyword");
        }
        if (suggestionProductFilter.getStoreId() != null) {
            sb.append(" AND e.storeId = :storeId");
        }
        sb.append(" ORDER BY random()");


        Pageable pageable = PageRequest.of(suggestionProductFilter.getPage(), suggestionProductFilter.getLimit());
        HibernateQueryResult<Product> queryResult = new HibernateQueryResult<>(em, Product.class, sb, pageable);
        return queryResult.getResultList(buildQueryParamsFromSuggestionProductFilter(suggestionProductFilter)).getContent();
    }

    private Map<String, Object> buildQueryParamsFromSuggestionProductFilter(SuggestionProductFilter suggestionProductFilter) {
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotBlank(suggestionProductFilter.getKeyword())) {
            params.put("keyword", DbUtil.buildSearchLikeQuery(suggestionProductFilter.getKeyword()));
        }

        if (suggestionProductFilter.getStoreId() != null) {
            params.put("storeId", suggestionProductFilter.getStoreId());
        }

        return params;
    }

    @Override
    public Page<NewFeedResponse> findNewFeedsOfStore(NewFeedsFilter newFeedsFilter) {
        Pageable pageable = PageRequest.of(newFeedsFilter.getPage(), newFeedsFilter.getLimit());
        NativeQueryResult<NewFeedResponse> nativeQueryResult = new NativeQueryResult<>(
                em, NewFeedResponse.class, newFeedsSpecificationBuilder.buildQuery(newFeedsFilter), pageable);
        return nativeQueryResult.getResultList(newFeedsSpecificationBuilder.buildParams(newFeedsFilter));
    }

    @Override
    public Page<Long> getProductIds(ProductIdFilter productIdFilter) {
        StringBuilder sb = new StringBuilder()
                .append(" SELECT e.id")
                .append(" FROM Product e")
                .append(" WHERE deletedAt is NULL");

        if (productIdFilter.getProductStatus() != null) {
            sb.append(" AND status = :status");
        }

        if (productIdFilter.getFromDate() != null) {
            sb.append(" AND e.updatedAt >= :fromDate");
        }

        if (productIdFilter.getToDate() != null) {
            sb.append(" AND e.updatedAt <= :toDate");
        }

        Pageable pageable = PageRequest.of(productIdFilter.getPage(), productIdFilter.getLimit());
        HibernateQueryResult<Long> queryResult = new HibernateQueryResult<>(em, Long.class, sb, pageable);
        return queryResult.getResultList(buildQueryParamsFromProductIdFilter(productIdFilter));
    }

    private Map<String, Object> buildQueryParamsFromProductIdFilter(ProductIdFilter productIdFilter) {
        Map<String, Object> params = new HashMap<>();

        if (productIdFilter.getProductStatus() != null) {
            params.put("status", productIdFilter.getProductStatus().getStatus());
        }

        if (productIdFilter.getFromDate() != null) {
            params.put("fromDate", productIdFilter.getFromDate());
        }

        if (productIdFilter.getToDate() != null) {
            params.put("toDate", productIdFilter.getToDate());
        }

        return params;
    }
}
