package vn.prostylee.store.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.StoreBanner;

import java.util.List;

@Repository
public interface StoreBannerRepository extends BaseRepository<StoreBanner, Long> {

    @Query("SELECT e FROM #{#entityName} e where e.store.id = :storeId")
    List<StoreBanner> findStoreBannerByStoreId(@Param("storeId") Long storeId);
}
