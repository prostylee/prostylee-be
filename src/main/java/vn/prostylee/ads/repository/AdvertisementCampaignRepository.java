package vn.prostylee.ads.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.ads.entity.AdvertisementCampaign;
import vn.prostylee.core.repository.BaseRepository;

@Repository
public interface AdvertisementCampaignRepository extends BaseRepository<AdvertisementCampaign, Long> {
}
