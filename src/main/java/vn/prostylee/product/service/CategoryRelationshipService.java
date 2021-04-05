package vn.prostylee.product.service;

import java.util.Set;

public interface CategoryRelationshipService {

    Set<Long> getCategoryIdsRelationshipWith(Long cagegoryId);
}
