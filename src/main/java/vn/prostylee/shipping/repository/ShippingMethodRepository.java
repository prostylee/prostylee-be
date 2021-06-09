package vn.prostylee.shipping.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.shipping.entity.ShippingMethod;

/**
 * Repository for domain model class ShippingType.
 * @see ShippingMethod ;
 * @author prostylee
 */
@Repository
public interface ShippingMethodRepository extends BaseRepository<ShippingMethod, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM shipping_provider WHERE shipping_method_id = :id)", nativeQuery = true)
    boolean isInUsed(@Param("id") Long shippingMethodId);
}
