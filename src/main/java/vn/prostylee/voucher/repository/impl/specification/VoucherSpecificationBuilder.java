package vn.prostylee.voucher.repository.impl.specification;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.ApiParamConstant;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;

import java.util.*;
import java.util.stream.Collectors;

import static vn.prostylee.store.constants.StoreConstant.PROSTYLEE_STORE;
import static vn.prostylee.voucher.dto.filter.VoucherUserFilter.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherSpecificationBuilder {

    private final AuthenticatedProvider authenticatedProvider;

    public StringBuilder buildQuery(VoucherUserFilter filter) {
        return new StringBuilder(buildSelectClause(filter))
                .append(buildFromClause(filter))
                .append(buildWhereClause(filter))
                .append(buildOrderClause(filter));
    }

    private StringBuilder buildSelectClause(VoucherUserFilter filter) {
        final StringBuilder sbSelect = new StringBuilder("SELECT v.*");
        sbSelect.append(" , vc.id AS savedUserVoucherId");
        return sbSelect;
    }

    private StringBuilder buildFromClause(VoucherUserFilter filter) {
        final StringBuilder sbFrom = new StringBuilder(" FROM voucher v");
        sbFrom.append(" LEFT JOIN voucher_user vc ON vc.voucher_id = v.id AND vc.created_by = :createdBy");
        return sbFrom;
    }

    private StringBuilder buildWhereClause(VoucherUserFilter filter) {
        final StringBuilder sbWhere = new StringBuilder(" WHERE 1=1");

        if (BooleanUtils.isTrue(filter.getSavedByMe())) {
            sbWhere.append(" AND vc.created_by = :createdBy");
        }

        if (filter.getStoreId() != null) {
            if (filter.getStoreId() == PROSTYLEE_STORE.getStoreId()) {
                sbWhere.append(" AND v.store_id = ").append(PROSTYLEE_STORE.getStoreId());
            } else {
                sbWhere.append(" AND v.store_id != ").append(PROSTYLEE_STORE.getStoreId());
            }
        }

        if (filter.getType() != null) {
            sbWhere.append(" AND v.type = :type");
        }

        if (StringUtils.isNotBlank(filter.getKeyword())) {
            sbWhere.append(" AND (LOWER(v.name) LIKE :keyword OR LOWER(v.code) LIKE :keyword)");
        }

        return sbWhere;
    }

    private StringBuilder buildOrderClause(VoucherUserFilter filter) {
        final StringBuilder sbOrder = new StringBuilder();

        final List<String> supportedSortFields = Arrays.asList(filter.getSortableFields());
        List<String> orderByColumns = Optional.ofNullable(filter.getSorts())
                .map(Arrays::asList)
                .filter(CollectionUtils::isNotEmpty)
                .orElseGet(() -> Collections.singletonList("-updatedAt"))
                .stream()
                .map(item -> {
                    if (item.startsWith(ApiParamConstant.SORT_DESC)) {
                        String columnName = item.substring(1);
                        if (isSupportedExtSort(columnName)) {
                            return buildExtSort(columnName) + " DESC";
                        } else if (supportedSortFields.contains(columnName)) {
                            return " v." + convertFromCamelToUnderscore(columnName) + " DESC";
                        }
                    } else {
                        if (isSupportedExtSort(item)) {
                            return buildExtSort(item);
                        } else if (supportedSortFields.contains(item)) {
                            return " v." + convertFromCamelToUnderscore(item);
                        }
                    }
                    return "";
                }).collect(Collectors.toList());
        String orderBySQL = String.join(", ", orderByColumns);

        if (orderBySQL.trim().length() > 0) {
            sbOrder.append(" ORDER BY").append(orderBySQL);
        } else {
            sbOrder.append(" ORDER BY v.updated_at DESC");
        }

        return sbOrder;
    }

    private String buildExtSort(String columnName) {
        if (SORT_BY_BEST_DISCOUNT.equalsIgnoreCase(columnName)) {
            return " COALESCE(v.discount_amount, COALESCE(v.discount_percent, 0)) ";
        }
        if (SORT_BY_EXPIRED_DATE.equalsIgnoreCase(columnName)) {
            return " (v.cnd_valid_to IS NULL), v.cnd_valid_to ";
        }
        if (SORT_BY_MOST_USED.equalsIgnoreCase(columnName)) {
            return "v.created_at"; // Consider created at as most used. // TODO count from user voucher with usedAt is not null
        }
        return "";
    }

    private boolean isSupportedExtSort(String columnName) {
        return EXT_SORTS.contains(columnName);
    }

    private String convertFromCamelToUnderscore(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    public Map<String, Object> buildParams(VoucherUserFilter filter) {
        Map<String, Object> params = new HashMap<>();

        params.put("createdBy", authenticatedProvider.getUserIdValue());

        if (filter.getType() != null) {
            params.put("type", filter.getType());
        }

        if (StringUtils.isNotBlank(filter.getKeyword())) {
            params.put("keyword", DbUtil.buildSearchLikeQuery(filter.getKeyword()));
        }

        return params;
    }
}
