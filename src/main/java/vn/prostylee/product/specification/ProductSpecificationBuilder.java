package vn.prostylee.product.specification;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.ApiParamConstant;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.ProductAttributeRepository;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSpecificationBuilder {
    private static final int NUMBER_OF_TOP_FOLLOWING_STORE = 50;
    private static final int NUMBER_OF_PAID_STORE = 30;
    private static final int NUMBER_OF_NEW_STORE = 100;
    private static final int NUMBER_OF_RECORD_TO_BE_CALCULATED_DISTANCE = 10000;

    private final UserFollowerService userFollowerService;
    private final StoreService storeService;
    private final ProductAttributeRepository productAttributeRepository;
    private final AttributeRepository attributeRepository;
    private static Map<Long,String> attributeTypes;

    public StringBuilder buildQuery(ProductFilter productFilter) {
        List<Long> storeIds = new ArrayList<>(Optional.ofNullable(productFilter.getStoreIds())
                .orElseGet(Collections::emptyList));
        if (BooleanUtils.isTrue(productFilter.getTopFollowingStore())) {
            storeIds.addAll(getTopFollowingStores(NUMBER_OF_TOP_FOLLOWING_STORE, productFilter));
        } else if (BooleanUtils.isTrue(productFilter.getPaidStore())) {
            storeIds.addAll(getPaidStores(NUMBER_OF_PAID_STORE, productFilter));
        } else if (BooleanUtils.isTrue(productFilter.getNewStore())) {
            storeIds.addAll(getNewStores(NUMBER_OF_NEW_STORE, productFilter));
        }
        productFilter.setStoreIds(storeIds);

        return new StringBuilder("SELECT p.*")
                .append(buildFromClause(productFilter))
                .append(buildWhereClause(productFilter))
                .append(buildOrderClause(productFilter));
    }

    private StringBuilder buildFromClause(ProductFilter productFilter) {
        final StringBuilder sbFrom = new StringBuilder(" FROM product p");

        if (productFilter.getCategoryId() != null) {
            sbFrom.append(" INNER JOIN category c ON c.id = p.category_id");
        } else if (StringUtils.isNotBlank(productFilter.getKeyword())) {
            sbFrom.append(" LEFT JOIN category c ON c.id = p.category_id");
        }

        if (isAttributesAvailable(productFilter.getAttributes())) {
            sbFrom.append(" INNER JOIN product_price pp ON pp.product_id = p.id");
        }

        if (BooleanUtils.isTrue(productFilter.getBestSeller()) || BooleanUtils.isTrue(productFilter.getBestRating())) {
            sbFrom.append(" LEFT JOIN product_statistic ps ON ps.id = p.id");
        }

        if (productFilter.getLatitude() != null && productFilter.getLongitude() != null) {
            sbFrom.append(" LEFT JOIN func_get_nearest_locations(:pLongitude, :pLatitude, :pTargetType, :pLimit, :pOffset) loc ON loc.id = p.location_id");
        }

        return sbFrom;
    }

    private StringBuilder buildWhereClause(ProductFilter productFilter) {
        final StringBuilder sbWhere = new StringBuilder(" WHERE 1=1");
        if (productFilter.getUserId() != null) {
            sbWhere.append(" AND p.created_by = :createdBy");
        }

        if (productFilter.getCategoryId() != null) {
            sbWhere.append(" AND p.category_id = :categoryId");
        }

        if (productFilter.getStoreId() != null || CollectionUtils.isNotEmpty(productFilter.getStoreIds())) {
            sbWhere.append(" AND p.store_id IN (:storeIds)");
        }

        if (isAttributesAvailable(productFilter.getAttributes())) {
            sbWhere.append(" AND pp.id IN (:productPriceIds)");
        }

        if (BooleanUtils.isTrue(productFilter.getSale())) {
            sbWhere.append(" AND p.price_sale IS NOT NULL AND p.price_sale < p.price");
        }

        if (StringUtils.isNotBlank(productFilter.getKeyword())) {
            sbWhere.append(" AND (LOWER(p.name) LIKE :keyword OR LOWER(c.name) LIKE :keyword)");
        }

        return sbWhere;
    }

    private StringBuilder buildOrderClause(ProductFilter productFilter) {
        final StringBuilder sbOrder = new StringBuilder();
        if (BooleanUtils.isTrue(productFilter.getSale())) {
            sbOrder.append(" ORDER BY (100 - (COALESCE(p.price_sale, 0) / p.price) * 100) DESC, p.created_at DESC");
        } else if (BooleanUtils.isTrue(productFilter.getBestSeller())) {
            sbOrder.append(" ORDER BY COALESCE(ps.number_of_sold, 0) DESC, p.created_at DESC");
        } else if (BooleanUtils.isTrue(productFilter.getBestRating())) {
            sbOrder.append(" ORDER BY COALESCE(ps.result_of_rating, 0) DESC, p.created_at DESC");
        } else if (productFilter.getLatitude() != null && productFilter.getLongitude() != null) {
            sbOrder.append(" ORDER BY COALESCE(loc.distance, 100000000) ASC, p.created_at DESC");
        } else {
            final List<String> supportedSortFields = Arrays.asList(productFilter.getSortableFields());
            List<String> orderByColumns = Optional.ofNullable(productFilter.getSorts())
                    .map(Arrays::asList)
                    .filter(CollectionUtils::isNotEmpty)
                    .orElseGet(() -> Collections.singletonList("-createdAt"))
                    .stream()
                    .map(item -> {
                        if (item.startsWith(ApiParamConstant.SORT_DESC)) {
                            String columnName = item.substring(1);
                            if (supportedSortFields.contains(columnName)) {
                                return " p." + convertFromCamelToUnderscore(columnName) + " DESC";
                            }
                        } else if (supportedSortFields.contains(item)) {
                            return " p." + convertFromCamelToUnderscore(item);
                        }
                        return "";
                    }).collect(Collectors.toList());
            String orderBySQL = String.join(", ", orderByColumns);
            if (orderBySQL.trim().length() > 0) {
                sbOrder.append(" ORDER BY").append(orderBySQL);
            } else {
                sbOrder.append(" ORDER BY p.created_at DESC");
            }
        }

        return sbOrder;
    }

    private String convertFromCamelToUnderscore(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public Map<String, Object> buildParams(ProductFilter productFilter) {
        Map<String, Object> params = new HashMap<>();

        if (productFilter.getUserId() != null) {
            params.put("createdBy", productFilter.getUserId());
        }

        if (productFilter.getCategoryId() != null) {
            params.put("categoryId", productFilter.getCategoryId());
        }

        if (productFilter.getStoreId() != null || CollectionUtils.isNotEmpty(productFilter.getStoreIds())) {
            List<Long> storeIds = new ArrayList<>(Optional.ofNullable(productFilter.getStoreIds())
                    .orElseGet(Collections::emptyList));
            if (productFilter.getStoreId() != null) {
                storeIds.add(productFilter.getStoreId());
            }
            params.put("storeIds", storeIds);
        }

        if (isAttributesAvailable(productFilter.getAttributes())) {
            List<Long> attrs = findByAttributes(productFilter.getAttributes());
            params.put("productPriceIds", attrs);
        }

        if (StringUtils.isNotBlank(productFilter.getKeyword())) {
            params.put("keyword", DbUtil.buildSearchLikeQuery(productFilter.getKeyword()));
        }

        if (productFilter.getLatitude() != null && productFilter.getLongitude() != null) {
            params.put("pLongitude", productFilter.getLongitude());
            params.put("pLatitude", productFilter.getLatitude());
            params.put("pTargetType", TargetType.PRODUCT.name());
            params.put("pLimit", NUMBER_OF_RECORD_TO_BE_CALCULATED_DISTANCE);
            params.put("pOffset", 0);
        }

        return params;
    }

    private List<Long> getNewStores(int numberOfStore, ProductFilter productFilter) {
        NewestStoreRequest request = NewestStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfStore);
        return storeService.getNewStoreIds(request);
    }

    private List<Long> getPaidStores(int numberOfStore, ProductFilter productFilter) {
        PaidStoreRequest request = PaidStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfStore);
        // TODO https://prostylee.atlassian.net/browse/BE-426
        return Collections.emptyList();
    }

    private List<Long> getTopFollowingStores(int numberOfStore, ProductFilter productFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfStore);
        return userFollowerService.getTopBeFollows(request);
    }

    private boolean isAttributesAvailable(Map<String, List<String>> attributes) {
        return MapUtils.isNotEmpty(attributes);
    }

    private List<Long> findByAttributes(Map<String, List<String>> attributesRequest) {
        if (MapUtils.isEmpty(attributeTypes)) {
            attributeTypes = new HashMap<>();
            List<Attribute> attrs = attributeRepository.findAll();
            attrs.forEach(attr -> attributeTypes.put(attr.getId(), attr.getKey()));
        }
        return productAttributeRepository.findCrossTabProductAttribute(attributesRequest, attributeTypes);
    }
}
