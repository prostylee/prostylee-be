package vn.prostylee.product.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.product.entity.UsedStatus;

@Repository
public interface UsedStatusRepository extends BaseRepository<UsedStatus, Long> {

}
