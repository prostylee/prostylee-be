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
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductExtRepository;
import vn.prostylee.product.specification.ProductSpecificationBuilder;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ProductExtRepositoryImpl implements ProductExtRepository {

    private final EntityManager em;
    private final ProductSpecificationBuilder productSpecificationBuilder;

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
        return queryResult.getResultList(buildQueryParams(suggestionProductFilter)).getContent();
    }

    private Map<String, Object> buildQueryParams(SuggestionProductFilter suggestionProductFilter) {
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotBlank(suggestionProductFilter.getKeyword())) {
            params.put("keyword", DbUtil.buildSearchLikeQuery(suggestionProductFilter.getKeyword()));
        }

        if (suggestionProductFilter.getStoreId() != null) {
            params.put("storeId", suggestionProductFilter.getStoreId());
        }

        return params;
    }
}
