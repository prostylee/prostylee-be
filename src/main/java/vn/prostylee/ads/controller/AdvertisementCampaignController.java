package vn.prostylee.ads.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.ads.dto.filter.AdvertisementCampaignFilter;
import vn.prostylee.ads.dto.request.AdvertisementCampaignRequest;
import vn.prostylee.ads.dto.response.AdvertisementCampaignResponse;
import vn.prostylee.ads.service.AdvertisementCampaignService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/ads-campaigns")
public class AdvertisementCampaignController extends CrudController<AdvertisementCampaignRequest, AdvertisementCampaignResponse, Long, AdvertisementCampaignFilter> {

    public AdvertisementCampaignController(AdvertisementCampaignService service) {
        super(service);
    }

}
