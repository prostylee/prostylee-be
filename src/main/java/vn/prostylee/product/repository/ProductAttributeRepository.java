package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.ProductAttribute;

import java.util.List;
import java.util.Map;

/**
 * Repository for domain model class ProductAttribute.
 * @see ProductAttribute;
 * @author prostylee
 */
@Repository
public interface ProductAttributeRepository extends BaseRepository<ProductAttribute, Long> {
    List<Long> findCrossTabProductAttribute(Map<String, List<String>> attributesRequest,
                                            Map<Long, String> attributeTypes);

    List<ProductAttribute> getProductAttributeByProductId(Long productId);

    List<ProductAttribute> getProductAttributeByPriceId(Long priceId);

}
