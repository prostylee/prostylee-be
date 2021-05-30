package vn.prostylee.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.store.service.StoreBannerService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.StoreBannerFilter;
import vn.prostylee.store.dto.request.StoreBannerRequest;
import vn.prostylee.store.dto.response.StoreBannerResponse;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/store-banners")
public class StoreBannerController extends CrudController<StoreBannerRequest, StoreBannerResponse, Long, StoreBannerFilter> {

    public StoreBannerController(StoreBannerService service) {
        super(service);
    }

}
