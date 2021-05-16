package vn.prostylee.store.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.Branch;

/**
 * Repository for domain model class Branch.
 * @see Branch;
 * @author prostylee
 */
@Repository
public interface BranchRepository extends BaseRepository<Branch, Long> {

}
