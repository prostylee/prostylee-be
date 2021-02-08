package vn.prostylee.location.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.location.dto.filter.LocationFilter;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/locations")
public class LocationController extends CrudController<LocationRequest, LocationResponse, Long, LocationFilter> {

    public LocationController(LocationService locationService) {
        super(locationService);
    }
}
