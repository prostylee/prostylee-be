package vn.prostylee.product.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import vn.prostylee.product.repository.ProductAttributeExtRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ProductAttributeExtRepositoryImpl implements ProductAttributeExtRepository {

    private final EntityManager em;

    @Override
    public List<Long> findCrossTabProductAttribute(Map<String, List<String>> attributesRequest,
                                                   Map<Long, String> attributeTypes) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT product_price_id")
                .append(" FROM crosstab('Select product_price_id, attr_id, attr_value")
                .append(" from product_attribute pa join attribute attr")
                .append(" on pa.attr_id = attr.id order by 1',")
                .append(" 'select id from attribute' )")
                .append(" AS product_attribute(product_price_id bigint,");
        if (MapUtils.isNotEmpty(attributesRequest)) {
            attributeTypes.forEach((k,v) -> stringBuilder.append(v + " varchar,"));
            stringBuilder.deleteCharAt(stringBuilder.toString().lastIndexOf(","));
            stringBuilder.append(") WHERE ");
        }
        for (Map.Entry attribute : attributesRequest.entrySet()) {
            stringBuilder.append("LOWER(" + attribute.getKey() + ") IN (:" + attribute.getKey() + ") AND ");
        }
        String sql = stringBuilder.substring(0, stringBuilder.toString().lastIndexOf("AND"));

        Query query = em.createNativeQuery(sql);
        setParameters(query, buildQueryParams(attributesRequest));

        return query.unwrap(NativeQuery.class).getResultList();
    }

    private Map<String, Object> buildQueryParams(Map<String, List<String>> attributes) {
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry attribute : attributes.entrySet()) {
            List<String> attrValue = buildMultipleValueForParam((ArrayList)attribute.getValue());
            params.put((String) attribute.getKey(), attrValue);
        }
        return params;
    }

    private List<String> buildMultipleValueForParam(List<String> attributeValues) {
        return attributeValues.stream().map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private void setParameters(Query query, Map<String, Object> params) {
        params.forEach(query::setParameter);
    }
}
