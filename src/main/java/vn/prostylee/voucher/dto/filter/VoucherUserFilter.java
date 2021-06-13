package vn.prostylee.voucher.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;
import vn.prostylee.core.dto.filter.BaseFilter;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherUserFilter extends BaseFilter {

    public static final String SORT_BY_BEST_DISCOUNT = "bestDiscount";
    public static final String SORT_BY_EXPIRED_DATE = "expiredDate";
    public static final String SORT_BY_MOST_USED = "mostUsed";
    public static final List<String> EXT_SORTS = List.of(SORT_BY_BEST_DISCOUNT, SORT_BY_EXPIRED_DATE, SORT_BY_MOST_USED);

    @Schema(description = "Null if find all stores, 0 if find by Prostylee, greater than 0 if find by store id")
    private Long storeId;

    private Integer type;

    private Boolean savedByMe;

    @Schema(description = "Supported sort by: name, code, type, updatedAt, bestDiscount, expiredDate, mostUsed. Multiple sort request parameters, for example: sorts=+updatedAt&sorts=-name")
    private String[] sorts;

    @Override
    public String[] getSortableFields() {
        String[] sortByColumns = {
                "name",
                "code",
                "type",
                "updatedAt",
        };
        return ArrayUtils.addAll(sortByColumns, EXT_SORTS.toArray(new String[0]));
    }
}
