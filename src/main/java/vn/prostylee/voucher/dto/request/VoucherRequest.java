package vn.prostylee.voucher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherRequest {

    @NotNull
    private Long storeId;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String code;

    @Builder.Default
    private Boolean active = false;

    @NotNull
    private Integer type;

    private Double discountAmount;

    private Double discountMaxAmount;

    private Integer discountPercent;

    private Integer cndProductType;

    private List<Long> cndProductProdIds;

    private List<Long> cndProductCatIds;

    private Integer cndOrderAmountType;

    private Double cndOrderAmountMinValue;

    private Integer cndQuantityType;

    private Integer cndQuantityMaxValue;

    private Integer cndCustomerType;

    private List<Long> cndCustomerUserIds;

    private List<Long> cndCustomerLocationIds;

    private Integer cndCouponQuantityType;

    private Integer cndCouponQuantityMaxValue;

    private Integer cndLimitedUseType;

    private Integer cndLimitedUseMaxValue;

    private Date cndValidFrom;

    private Date cndValidTo;

    private Integer cndShippingMethodType;

    private Integer cndShippingProviderType;

    private List<Long> cndShippingProviderIds;

    private Integer cndPaymentType;

    private Integer cndBuyType;

    private Boolean cndApplyMultipleCoupons;

    private Date deletedAt;
}
