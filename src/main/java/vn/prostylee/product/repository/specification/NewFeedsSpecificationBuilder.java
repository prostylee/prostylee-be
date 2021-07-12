package vn.prostylee.product.repository.specification;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.ApiParamConstant;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.ProductAttributeRepository;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewFeedsSpecificationBuilder {
    private static final int NUMBER_OF_TOP_FOLLOWING = 50;
    private static final int DEFAULT_TIME_RANGE_IN_DAYS = 90;

    private final UserFollowerService userFollowerService;
    private final AuthenticatedProvider authenticatedProvider;

    public StringBuilder buildQuery(NewFeedsFilter newFeedsFilter) {
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            List<Long> listIdsFollowing = this.getListObjectFollowing(authenticatedProvider.getUserIdValue(), TargetType.USER.name());
            if(CollectionUtils.isEmpty(listIdsFollowing)){
                listIdsFollowing = this.getTopFollowing(NUMBER_OF_TOP_FOLLOWING, TargetType.USER.name(), DEFAULT_TIME_RANGE_IN_DAYS);
            }
            newFeedsFilter.setUserIds(listIdsFollowing);
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            List<Long> listIdsFollowing = this.getListObjectFollowing(authenticatedProvider.getUserIdValue(), TargetType.STORE.name());
            if(CollectionUtils.isEmpty(listIdsFollowing)){
                listIdsFollowing = this.getTopFollowing(NUMBER_OF_TOP_FOLLOWING, TargetType.STORE.name(), DEFAULT_TIME_RANGE_IN_DAYS);
            }
            newFeedsFilter.setStoreIds(listIdsFollowing);
        }

        if (CollectionUtils.isEmpty(newFeedsFilter.getUserIds()) && CollectionUtils.isEmpty(newFeedsFilter.getStoreIds())){
            return new StringBuilder("SELECT nf.*")
                    .append(buildFromClauseForAllPostAndProduct(newFeedsFilter))
                    .append(buildWhereClause(newFeedsFilter))
                    .append(buildOrderClause(newFeedsFilter));
        }
        return new StringBuilder("SELECT nf.*")
                .append(buildFromClause(newFeedsFilter))
                .append(buildWhereClause(newFeedsFilter))
                .append(buildOrderClause(newFeedsFilter));
    }

    private StringBuilder buildFromClause(NewFeedsFilter newFeedsFilter) {
        final StringBuilder sbFrom = new StringBuilder(" FROM (");

        sbFrom.append(" SELECT 'POST' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at");
        if (TargetType.valueOf(newFeedsFilter.getTargetType())  == TargetType.USER){
            sbFrom.append(" , rank() OVER (PARTITION BY p.created_by ORDER BY p.created_at DESC) AS rank");
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType())  == TargetType.STORE){
            sbFrom.append(" , rank() OVER (PARTITION BY p.store_owner ORDER BY p.created_at DESC) AS rank");
            sbFrom.append(" , p.store_owner AS owner_id");
        }
        sbFrom.append(" , 1 AS priority");
        sbFrom.append(" FROM post p");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE p.created_by IN (:userIds) AND p.store_owner IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE p.store_owner IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT 'PRODUCT' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" , rank() OVER (PARTITION BY pr.created_by ORDER BY pr.created_at DESC) AS rank");
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" , rank() OVER (PARTITION BY pr.store_id ORDER BY pr.created_at DESC) AS rank");
            sbFrom.append(" , pr.store_id AS owner_id");
        }
        sbFrom.append(" , 1 AS priority");
        sbFrom.append(" FROM product pr");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE pr.created_by IN (:userIds) AND pr.store_id IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE pr.store_id IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT 'POST' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at" +
                " , 0 AS rank");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" , p.store_owner AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM post p");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE p.created_by NOT IN (:userIds) AND p.store_owner IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE p.store_owner NOT IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT 'PRODUCT' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at" +
                " , 0 AS rank");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" , pr.store_id AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM product pr");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE pr.created_by NOT IN (:userIds) AND pr.store_id IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE pr.store_id NOT IN (:storeIds)");
        }

        sbFrom.append(" ) AS nf");
        return sbFrom;
    }

    private StringBuilder buildFromClauseForAllPostAndProduct(NewFeedsFilter newFeedsFilter) {
        final StringBuilder sbFrom = new StringBuilder(" FROM (");

        sbFrom.append(" SELECT 'POST' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at" +
                " , 0 AS rank");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" , p.store_owner AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM post p");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE p.store_owner IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE p.store_owner IS NOT NULL");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT 'PRODUCT' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at" +
                " , 0 AS rank");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" , pr.store_id AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM product pr");
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER){
            sbFrom.append(" WHERE pr.store_id IS NULL");
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE){
            sbFrom.append(" WHERE pr.store_id IS NOT NULL");
        }

        sbFrom.append(" ) AS nf");
        return sbFrom;
    }

    private StringBuilder buildWhereClause(NewFeedsFilter newFeedsFilter) {
        final StringBuilder sbWhere = new StringBuilder(" WHERE nf.rank < 2 ");
        return sbWhere;
    }

    private StringBuilder buildOrderClause(NewFeedsFilter newFeedsFilter) {
        final StringBuilder sbOrder = new StringBuilder(" ORDER BY nf.priority DESC, nf.created_at DESC");
        return sbOrder;
    }

    private String convertFromCamelToUnderscore(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public Map<String, Object> buildParams(NewFeedsFilter newFeedsFilter) {
        Map<String, Object> params = new HashMap<>();

        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.STORE
                && CollectionUtils.isNotEmpty(newFeedsFilter.getStoreIds())) {
            List<Long> storeIds = new ArrayList<>(Optional.ofNullable(newFeedsFilter.getStoreIds())
                    .orElseGet(Collections::emptyList));
            params.put("storeIds", storeIds);
        }
        if (TargetType.valueOf(newFeedsFilter.getTargetType()) == TargetType.USER
                && CollectionUtils.isNotEmpty(newFeedsFilter.getUserIds())) {
            List<Long> userIds = new ArrayList<>(Optional.ofNullable(newFeedsFilter.getUserIds())
                    .orElseGet(Collections::emptyList));
            params.put("userIds", userIds);
        }
        return params;
    }

    private List<Long> getListObjectFollowing(Long userId, String targetType) {
        UserFollowerFilter userFollowerFilter = UserFollowerFilter.builder()
                .userId(userId)
                .targetType(targetType)
                .build();
        Page<UserFollowerResponse> page = userFollowerService.findAll(userFollowerFilter);
        List<Long> ids = page.getContent()
                .stream()
                .map(UserFollowerResponse::getTargetId)
                .collect(Collectors.toList());
        return ids;
    }

    private List<Long> getTopFollowing(int numberOfStore,String targetType, int numberOfDays) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(targetType))
                .fromDate(DateUtils.getLastDaysBefore(numberOfDays))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfStore);
        return userFollowerService.getTopBeFollows(request);
    }
}
