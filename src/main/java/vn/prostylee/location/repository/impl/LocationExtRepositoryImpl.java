package vn.prostylee.location.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.core.repository.query.NativeQueryResult;
import vn.prostylee.location.dto.DistanceWrapper;
import vn.prostylee.location.dto.LatLngDto;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.entity.Location;
import vn.prostylee.location.repository.LocationExtRepository;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vn.prostylee.core.dto.filter.PagingParam.NUMBER_OF_RECORD_DEFAULT;

@Repository
public class LocationExtRepositoryImpl extends BaseRepositoryImpl<Location, Long> implements LocationExtRepository {

    public LocationExtRepositoryImpl(EntityManager em) {
        super(Location.class, em);
    }

    @Override
    public List<LocationResponse> getNearestLocations(NearestLocationFilter nearestLocationFilter) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT id, address, latitude, longitude, state, city, country, zipcode, distance")
                .append(" FROM func_get_nearest_locations(:pLongitude, :pLatitude, :pTargetType, :pLimit, :pOffset");
        if (StringUtils.isNotBlank(nearestLocationFilter.getKeyword())) {
            stringBuilder.append(", :pKeyword");
        }
        stringBuilder.append(")");

        NativeQueryResult<LocationResponse> nativeQueryResult = new NativeQueryResult<>(getEntityManager(), LocationResponse.class, stringBuilder);
        return nativeQueryResult.getResultList(buildQueryParams(nearestLocationFilter)).getContent();
    }

    @Override
    public Double calculateDistance(LatLngDto from, LatLngDto to) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" SELECT func_get_distance_from_lat_lng_in_km(:fromLat, :fromLng, :toLat, :toLng) AS distance");

        Map<String, Object> params = new HashMap<>();
        params.put("fromLat", from.getLatitude());
        params.put("fromLng", from.getLongitude());
        params.put("toLat", to.getLatitude());
        params.put("toLng", to.getLongitude());

        NativeQueryResult<DistanceWrapper> nativeQueryResult = new NativeQueryResult<>(getEntityManager(), DistanceWrapper.class, stringBuilder);
        return nativeQueryResult.getSingleResult(params)
                .map(DistanceWrapper::getDistance)
                .orElse(0d);
    }

    private Map<String, Object> buildQueryParams(NearestLocationFilter nearestLocationFilter) {
        int limit = nearestLocationFilter.getLimit();
        if (limit <= 0) {
            limit = NUMBER_OF_RECORD_DEFAULT;
        }

        int offset = nearestLocationFilter.getPage();
        if (nearestLocationFilter.getPage() < 0) {
            offset = 0;
        }
        offset = offset * limit;

        Map<String, Object> params = new HashMap<>();
        params.put("pLongitude", nearestLocationFilter.getLongitude());
        params.put("pLatitude", nearestLocationFilter.getLatitude());
        params.put("pTargetType", nearestLocationFilter.getTargetType());
        if (StringUtils.isNotBlank(nearestLocationFilter.getKeyword())) {
            params.put("pKeyword", nearestLocationFilter.getKeyword());
        }
        params.put("pLimit", limit);
        params.put("pOffset", offset);
        return params;
    }
}
