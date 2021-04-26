package vn.prostylee.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.product.constant.PagingConstant;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class NewestProductRequest extends PagingParam {

    private Long storeId;

    private Date fromDate;

    private Date toDate;

    @Schema(name = "numberOfProducts", example = "10", description = "Number of products in each store to be received.")
    @Range(min = 1, max = 100)
    @Builder.Default
    private int numberOfProducts = PagingConstant.DEFAULT_NUMBER_OF_PRODUCT_IN_EACH_STORE;

    public NewestProductRequest pagingParam(PagingParam paging) {
        setLimit(paging.getLimit() <= 0 ? PagingConstant.DEFAULT_NUMBER_OF_PRODUCT : paging.getLimit());
        setPage(paging.getPage());
        return this;
    }
}
