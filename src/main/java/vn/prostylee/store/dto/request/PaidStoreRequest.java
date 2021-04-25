package vn.prostylee.store.dto.request;

import lombok.*;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.product.constant.PagingConstant;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class PaidStoreRequest extends PagingParam {

    private Long buyerId;

    private Date fromDate;

    private Date toDate;

    public PaidStoreRequest pagingParam(PagingParam paging) {
        setLimit(paging.getLimit() <= 0 ? PagingConstant.DEFAULT_NUMBER_OF_PRODUCT : paging.getLimit());
        setPage(paging.getPage());
        return this;
    }
}

