package vn.prostylee.ads.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.ads.entity.AdvertisementGroup;
import vn.prostylee.core.repository.BaseRepository;

@Repository
public interface AdvertisementGroupRepository extends BaseRepository<AdvertisementGroup, Long> {
}
