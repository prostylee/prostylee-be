package vn.prostylee.location.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.location.dto.LatLngDto;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.dto.response.LocationResponseLite;

import java.util.List;

public interface LocationService extends CrudService<LocationRequest, LocationResponse, Long> {

    LocationResponseLite getLocationResponseLite(Long id);

    List<LocationResponse> getNearestLocations(NearestLocationFilter nearestLocationFilter);

    LocationResponse getLocationByIdWithDistanceCalculation(Long id, LatLngDto latLngDto);
}
