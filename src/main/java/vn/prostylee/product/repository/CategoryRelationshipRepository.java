package vn.prostylee.product.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.CategoryRelationship;

import java.util.List;

/**
 * Repository for domain model class CategoryRelationship.
 * @see CategoryRelationship;
 * @author prostylee
 */
@Repository
public interface CategoryRelationshipRepository extends BaseRepository<CategoryRelationship, Long> {

    @Query("SELECT e.categoryId1 FROM #{#entityName} e WHERE e.categoryId2 = :categoryId")
    List<Long> getCategoryId1sRelationshipWith(@Param("categoryId") Long categoryId2);

    @Query("SELECT e.categoryId2 FROM #{#entityName} e WHERE e.categoryId1 = :categoryId")
    List<Long> getCategoryId2sRelationshipWith(@Param("categoryId") Long categoryId1);
}
