package vn.prostylee.ads.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.ads.entity.AdvertisementBanner;
import vn.prostylee.core.repository.BaseRepository;

@Repository
public interface AdvertisementBannerRepository extends BaseRepository<AdvertisementBanner, Long> {
}
