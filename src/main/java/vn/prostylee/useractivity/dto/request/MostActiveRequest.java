package vn.prostylee.useractivity.dto.request;

import lombok.*;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.product.constant.PagingConstant;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MostActiveRequest extends PagingParam {

    private List<TargetType> targetTypes;

    private Long customFieldId1;

    private Long customFieldId2;

    private Long customFieldId3;

    private Date fromDate;

    private Date toDate;

    public MostActiveRequest pagingParam(PagingParam paging) {
        setLimit(paging.getLimit() <= 0 ? PagingConstant.DEFAULT_NUMBER_OF_PRODUCT : paging.getLimit());
        setPage(paging.getPage());
        return this;
    }
}
