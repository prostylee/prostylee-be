package vn.prostylee.product.repository.impl;

import org.apache.commons.collections4.MapUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.repository.ProductAttributeRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductAttributeRepositoryImpl extends BaseRepositoryImpl<ProductAttribute, Long> implements ProductAttributeRepository {

    public ProductAttributeRepositoryImpl(EntityManager em) {
        super(ProductAttribute.class, em);
    }

    @Override
    public List<Long> findCrossTabProductAttribute(Map<String, String> attributesRequest) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT product_price_id")
                .append(" FROM crosstab('Select product_price_id, attr_id, attr_value")
                .append(" from product_attribute where attr_id=1 or attr_id=2 or attr_id=3 or attr_id=4 or attr_id=5 ')")
                .append(" AS product_attribute(product_price_id bigint, color varchar, size varchar, status varchar, material varchar, style varchar)");
        if(MapUtils.isNotEmpty(attributesRequest)) {
            stringBuilder.append(" WHERE ");
        }
        for (Map.Entry attribute : attributesRequest.entrySet()) {
            stringBuilder.append(attribute.getKey() + "=:" + attribute.getKey() + " AND ");
        }
        String sql = stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf("AND"));

        Query query = getEntityManager().createNativeQuery(sql);
        setParameters(query, buildQueryParams(attributesRequest));

        return query.unwrap(NativeQuery.class).getResultList();
    }

    private Map<String, Object> buildQueryParams(Map<String, String> attributes) {
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry attribute : attributes.entrySet()) {
            params.put((String) attribute.getKey(), attribute.getValue());
        }
        return params;
    }

    private void setParameters(Query query, Map<String, Object> params) {
        params.forEach(query::setParameter);
    }
}
