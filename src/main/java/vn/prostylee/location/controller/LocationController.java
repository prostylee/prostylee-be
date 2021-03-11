package vn.prostylee.location.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.location.dto.filter.LocationFilter;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/locations")
public class LocationController extends CrudController<LocationRequest, LocationResponse, Long, LocationFilter> {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        super(locationService);
        this.locationService = locationService;
    }

    @GetMapping("/nearest")
    public List<LocationResponse> getNearestLocations(@Valid NearestLocationFilter nearestLocationFilter) {
        return locationService.getNearestLocations(nearestLocationFilter);
    }
}
