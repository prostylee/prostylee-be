package vn.prostylee.product.repository;

import java.util.List;
import java.util.Map;

public interface ProductAttributeExtRepository {

    List<Long> findCrossTabProductAttribute(Map<String, List<String>> attributesRequest, Map<Long, String> attributeTypes);
}
