package vn.prostylee.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.Branch;

import java.util.List;

/**
 * Repository for domain model class Branch.
 * @see Branch;
 * @author prostylee
 */
@Repository
public interface BranchRepository extends BaseRepository<Branch, Long> {

    @Query("SELECT e.cityCode FROM #{#entityName} e where e.store.id = :storeId GROUP BY e.cityCode")
    List<String> geCitiesByStoreId(@Param("storeId") Long storeId);

}
