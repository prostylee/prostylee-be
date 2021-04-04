package vn.prostylee.store.dto.request;


import lombok.*;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.product.constant.PagingConstant;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class NewestStoreRequest extends PagingParam {

    private Date fromDate;

    private Date toDate;

    public NewestStoreRequest pagingParam(PagingParam paging) {
        setLimit(paging.getLimit() <= 0 ? PagingConstant.DEFAULT_NUMBER_OF_PRODUCT : paging.getLimit());
        setPage(paging.getPage());
        return this;
    }
}
