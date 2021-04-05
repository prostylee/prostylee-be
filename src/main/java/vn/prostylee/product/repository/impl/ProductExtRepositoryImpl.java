package vn.prostylee.product.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductExtRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductExtRepositoryImpl implements ProductExtRepository {

    private final EntityManager em;

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

        TypedQuery<Product> query = em
                .createQuery(sb.toString(), Product.class);

        if (StringUtils.isNotBlank(suggestionProductFilter.getKeyword())) {
            query.setParameter("keyword", DbUtil.buildSearchLikeQuery(suggestionProductFilter.getKeyword()));
        }
        if (suggestionProductFilter.getStoreId() != null) {
            query.setParameter("storeId", suggestionProductFilter.getStoreId());
        }

        return query.setFirstResult(suggestionProductFilter.getPage() * suggestionProductFilter.getLimit())
                .setMaxResults(suggestionProductFilter.getLimit())
                .getResultList();
    }
}
