package vn.prostylee.store.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.StoreStatistic;

@Repository
public interface StoreStatisticRepository extends BaseRepository<StoreStatistic, Long> {

}
