package vn.prostylee.ads.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.ads.dto.filter.AdvertisementBannerFilter;
import vn.prostylee.ads.dto.request.AdvertisementBannerRequest;
import vn.prostylee.ads.dto.response.AdvertisementBannerResponse;
import vn.prostylee.ads.service.AdvertisementBannerService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/ads-banners")
public class AdvertisementBannerController extends CrudController<AdvertisementBannerRequest, AdvertisementBannerResponse, Long, AdvertisementBannerFilter> {

    public AdvertisementBannerController(AdvertisementBannerService service) {
        super(service);
    }

}
