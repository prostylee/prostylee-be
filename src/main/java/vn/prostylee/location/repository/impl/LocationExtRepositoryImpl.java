package vn.prostylee.location.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.entity.Location;
import vn.prostylee.location.repository.LocationExtRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

        Query query = getEntityManager().createNativeQuery(stringBuilder.toString());
        setParameters(query, buildQueryParams(nearestLocationFilter));
        return query
                .unwrap(NativeQuery.class)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("address", StringType.INSTANCE)
                .addScalar("latitude", DoubleType.INSTANCE)
                .addScalar("longitude", DoubleType.INSTANCE)
                .addScalar("state", StringType.INSTANCE)
                .addScalar("city", StringType.INSTANCE)
                .addScalar("country", StringType.INSTANCE)
                .addScalar("zipcode", StringType.INSTANCE)
                .addScalar("distance", DoubleType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(LocationResponse.class))
                .getResultList();
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

    private void setParameters(Query query, Map<String, Object> params) {
        params.forEach(query::setParameter);
    }
}
