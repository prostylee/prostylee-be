package vn.prostylee.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {

    @NotNull
    private Long storeId;

    private Long branchId;

    @NotNull
    private Long productId;

    private Double productPrice;

    @NotNull
    @Min(1)
    private Integer amount;

    private String productName;

    private String productImage;

    private List<Long> productAttrIds;

}
