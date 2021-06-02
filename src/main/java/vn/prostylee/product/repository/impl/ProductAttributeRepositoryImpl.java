package vn.prostylee.product.repository.impl;

import org.apache.commons.collections4.MapUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.core.repository.query.HibernateQueryResult;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.repository.ProductAttributeRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductAttributeRepositoryImpl extends BaseRepositoryImpl<ProductAttribute, Long> implements ProductAttributeRepository {

    public ProductAttributeRepositoryImpl(EntityManager em) {
        super(ProductAttribute.class, em);
    }

    @Override
    public List<Long> findCrossTabProductAttribute(Map<String, List<String>> attributesRequest) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT product_price_id")
                .append(" FROM crosstab('Select product_price_id, attr_id, attr_value")
                .append(" from product_attribute where attr_id=1 or attr_id=2 or attr_id=3 or attr_id=4 or attr_id=5 ')")
                .append(" AS product_attribute(product_price_id bigint, color varchar, size varchar, status varchar, material varchar, style varchar)");
        if (MapUtils.isNotEmpty(attributesRequest)) {
            stringBuilder.append(" WHERE ");
        }
        for (Map.Entry attribute : attributesRequest.entrySet()) {
            stringBuilder.append("LOWER(" + attribute.getKey() + ") IN (:" + attribute.getKey() + ") AND ");
        }
        String sql = stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf("AND"));

        Query query = getEntityManager().createNativeQuery(sql);
        setParameters(query, buildQueryParams(attributesRequest));

        return query.unwrap(NativeQuery.class).getResultList();
    }

    @Override
    public List<ProductAttribute> getProductAttributeByProductId(Long productId) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT pa ")
                .append(" FROM ProductAttribute pa ")
                .append(" WHERE pa.product.id = :productId ")
                .append(" ORDER BY pa.attribute.id ");
        HibernateQueryResult<ProductAttribute> queryResult = new HibernateQueryResult<>(this.getEntityManager(),ProductAttribute.class,stringBuilder);
        return queryResult.getResultList(buildQueryParamProductId(productId)).getContent();
    }

    @Override
    public List<ProductAttribute> getProductAttributeByPriceId(Long priceId){
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT pa ")
                .append(" FROM ProductAttribute pa ")
                .append(" WHERE pa.productPrice.id = :priceId ")
                .append(" ORDER BY pa.id ");
        HibernateQueryResult<ProductAttribute> queryResult = new HibernateQueryResult<>(this.getEntityManager(),ProductAttribute.class,stringBuilder);
        return queryResult.getResultList(buildQueryParamPriceId(priceId)).getContent();
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

    private Map<String, Object> buildQueryParamProductId(Long productId) {
        Map<String, Object> params = new HashMap<>();

        if (productId != null) {
            params.put("productId", productId);
        }
        return params;
    }

    private Map<String, Object> buildQueryParamPriceId(Long priceId) {
        Map<String, Object> params = new HashMap<>();

        if (priceId != null) {
            params.put("priceId", priceId);
        }
        return params;
    }
}
