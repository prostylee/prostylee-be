package vn.prostylee.ads.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.ads.dto.filter.AdvertisementGroupFilter;
import vn.prostylee.ads.dto.request.AdvertisementGroupRequest;
import vn.prostylee.ads.dto.response.AdvertisementGroupResponse;
import vn.prostylee.ads.service.AdvertisementGroupService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/ads-groups")
public class AdvertisementGroupController extends CrudController<AdvertisementGroupRequest, AdvertisementGroupResponse, Long, AdvertisementGroupFilter> {

    public AdvertisementGroupController(AdvertisementGroupService service) {
        super(service);
    }

}
