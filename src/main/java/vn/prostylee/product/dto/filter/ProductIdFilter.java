package vn.prostylee.product.dto.filter;

import lombok.*;
import lombok.experimental.Accessors;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.product.constant.ProductStatus;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductIdFilter extends PagingParam {

    private Date fromDate;
    private Date toDate;
    private ProductStatus productStatus;
}
