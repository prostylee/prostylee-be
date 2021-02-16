package vn.prostylee.location.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.filter.LocationFilter;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.entity.Location;
import vn.prostylee.location.repository.LocationRepository;
import vn.prostylee.location.service.LocationService;

@Slf4j
@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    private final BaseFilterSpecs<Location> baseFilterSpecs;

    @Override
    public Page<LocationResponse> findAll(BaseFilter baseFilter) {
        LocationFilter locationFilter = (LocationFilter) baseFilter;
        Specification<Location> spec = baseFilterSpecs.search(baseFilter);
        Pageable pageable = baseFilterSpecs.page(locationFilter);
        Page<Location> page = locationRepository.findAll(spec, pageable);
        return page.map(this::convertToResponse);
    }

    @Override
    public LocationResponse findById(Long id) {
        Location location = getById(id);
        return convertToResponse(location);
    }

    private LocationResponse convertToResponse(Location location) {
        return BeanUtil.copyProperties(location, LocationResponse.class);
    }

    private Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location is not found with id [" + id + "]"));
    }

    @Override
    public LocationResponse save(LocationRequest request) {
        Location location = BeanUtil.copyProperties(request, Location.class);
        Location savedLocation = locationRepository.save(location);
        return convertToResponse(savedLocation);
    }

    @Override
    public LocationResponse update(Long id, LocationRequest request) {
        Location location = getById(id);
        BeanUtil.mergeProperties(request, location);
        Location savedLocation = locationRepository.save(location);
        return convertToResponse(savedLocation);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            locationRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.debug("Delete a company without existing in database", e);
            return false;
        }
    }
}
