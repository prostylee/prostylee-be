package vn.prostylee.location.repository;

import org.springframework.data.repository.NoRepositoryBean;
import vn.prostylee.location.dto.LatLngDto;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.response.LocationResponse;

import java.util.List;

@NoRepositoryBean
public interface LocationExtRepository extends LocationRepository {

    List<LocationResponse> getNearestLocations(NearestLocationFilter nearestLocationFilter);

    Double calculateDistance(LatLngDto from, LatLngDto to);
}
