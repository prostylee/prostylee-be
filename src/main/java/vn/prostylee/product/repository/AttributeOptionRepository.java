package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.AttributeOption;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for domain model class AttributeOption.
 * @see AttributeOption;
 * @author prostylee
 */
@Repository
public interface AttributeOptionRepository extends BaseRepository<AttributeOption, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.attribute.id = :attributeId")
    List<AttributeOption> getOptionByAttrId(@Param("attributeId") Long attributeId);

}
