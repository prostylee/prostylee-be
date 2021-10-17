package vn.prostylee.product.repository.specification;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
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
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            List<Long> listIdsFollowing = this.getListObjectFollowing(authenticatedProvider.getUserIdValue(), TargetType.USER);
            if (CollectionUtils.isEmpty(listIdsFollowing)) {
                listIdsFollowing = this.getTopFollowing(NUMBER_OF_TOP_FOLLOWING, TargetType.USER, DEFAULT_TIME_RANGE_IN_DAYS);
            }
            newFeedsFilter.setUserIds(listIdsFollowing);
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            List<Long> listIdsFollowing = this.getListObjectFollowing(authenticatedProvider.getUserIdValue(), TargetType.STORE);
            if (CollectionUtils.isEmpty(listIdsFollowing)) {
                listIdsFollowing = this.getTopFollowing(NUMBER_OF_TOP_FOLLOWING, TargetType.STORE, DEFAULT_TIME_RANGE_IN_DAYS);
            }
            newFeedsFilter.setStoreIds(listIdsFollowing);
        }

        if (CollectionUtils.isEmpty(newFeedsFilter.getUserIds()) && CollectionUtils.isEmpty(newFeedsFilter.getStoreIds())) {
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

        sbFrom.append(" SELECT '" + TargetType.POST.name() + "' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , p.store_id AS store_ads_id" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , rank() OVER (PARTITION BY p.created_by ORDER BY p.created_at DESC) AS rank");
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , rank() OVER (PARTITION BY p.store_owner ORDER BY p.created_at DESC) AS rank");
            sbFrom.append(" , COALESCE(p.store_owner, p.created_by) AS owner_id");
        }
        sbFrom.append(" , 1 AS priority");
        sbFrom.append(" FROM post p");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE p.created_by IN (:userIds) AND p.store_owner IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" WHERE p.store_owner IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT '" + TargetType.PRODUCT.name() + "' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , 0 AS store_ads_id" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , rank() OVER (PARTITION BY pr.created_by ORDER BY pr.created_at DESC) AS rank");
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , rank() OVER (PARTITION BY pr.store_id ORDER BY pr.created_at DESC) AS rank");
            sbFrom.append(" , COALESCE(pr.store_id, pr.created_by) AS owner_id");
        }
        sbFrom.append(" , 1 AS priority");
        sbFrom.append(" FROM product pr");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE pr.created_by IN (:userIds) AND pr.store_id IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" WHERE pr.store_id IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT '" + TargetType.POST.name() + "' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , p.store_id AS store_ads_id" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at" +
                " , 0 AS rank");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , COALESCE(p.store_owner, p.created_by) AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM post p");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE p.created_by NOT IN (:userIds) AND p.store_owner IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" WHERE p.store_owner NOT IN (:storeIds)");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT '" + TargetType.PRODUCT.name() + "' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , 0 AS store_ads_id" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at" +
                " , 0 AS rank");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , COALESCE(pr.store_id, pr.created_by) AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM product pr");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE pr.created_by NOT IN (:userIds) AND pr.store_id IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" WHERE pr.store_id NOT IN (:storeIds)");
        }

        sbFrom.append(" ) AS nf");
        return sbFrom;
    }

    private StringBuilder buildFromClauseForAllPostAndProduct(NewFeedsFilter newFeedsFilter) {
        final StringBuilder sbFrom = new StringBuilder(" FROM (");

        sbFrom.append(" SELECT '" + TargetType.POST.name() + "' AS type" +
                " , p.id AS id" +
                " , p.description AS content" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price" +
                " , CAST('0.0'AS DOUBLE PRECISION) AS price_sale" +
                " , CAST(p.created_at AS date) AS created_at" +
                " , 0 AS rank");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , p.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , COALESCE(p.store_owner, p.created_by) AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM post p");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE p.store_owner IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" WHERE p.store_owner IS NOT NULL");
        }

        sbFrom.append(" UNION");
        sbFrom.append(" SELECT '" + TargetType.PRODUCT.name() + "' AS type" +
                " , pr.id AS id" +
                " , pr.name AS content" +
                " , CAST(pr.price AS DOUBLE PRECISION) AS price" +
                " , CAST(pr.price_sale AS DOUBLE PRECISION) AS price_sale" +
                " , pr.created_at AS created_at" +
                " , 0 AS rank");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" , pr.created_by AS owner_id");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
            sbFrom.append(" , COALESCE(pr.store_id, pr.created_by) AS owner_id");
        }
        sbFrom.append(" , 0 AS priority");
        sbFrom.append(" FROM product pr");
        if (newFeedsFilter.getNewFeedType() == TargetType.USER) {
            sbFrom.append(" WHERE pr.store_id IS NULL");
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.STORE) {
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

        if (newFeedsFilter.getNewFeedType() == TargetType.STORE
                && CollectionUtils.isNotEmpty(newFeedsFilter.getStoreIds())) {
            List<Long> storeIds = new ArrayList<>(Optional.ofNullable(newFeedsFilter.getStoreIds())
                    .orElseGet(Collections::emptyList));
            params.put("storeIds", storeIds);
        }
        if (newFeedsFilter.getNewFeedType() == TargetType.USER
                && CollectionUtils.isNotEmpty(newFeedsFilter.getUserIds())) {
            List<Long> userIds = new ArrayList<>(Optional.ofNullable(newFeedsFilter.getUserIds())
                    .orElseGet(Collections::emptyList));
            params.put("userIds", userIds);
        }
        return params;
    }

    private List<Long> getListObjectFollowing(Long userId, TargetType targetType) {
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

    private List<Long> getTopFollowing(int numberOfStore, TargetType targetType, int numberOfDays) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(targetType))
                .fromDate(DateUtils.getLastDaysBefore(numberOfDays))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfStore);
        return userFollowerService.getTopBeFollows(request);
    }
}
