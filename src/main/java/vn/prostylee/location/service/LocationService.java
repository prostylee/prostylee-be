package vn.prostylee.location.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.dto.response.LocationResponseLite;

public interface LocationService extends CrudService<LocationRequest, LocationResponse, Long> {
    LocationResponseLite getLocationResponseLite(Long id);
}
