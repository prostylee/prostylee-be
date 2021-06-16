package vn.prostylee.voucher.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import vn.prostylee.store.dto.response.StoreResponseLite;

import java.util.Date;

@Data
public class VoucherUserResponse {

    private Long id;

    private String name;

    private String description;

    private String code;

    @Schema(description = "type=1: Discount by fixed amount, type=2: Discount by percent.")
    private Integer type;

    private Double discountAmount;

    private Double discountMaxAmount;

    private Integer discountPercent;

    private Integer cndProductType;

    private String cndProductProdIds;

    private String cndProductCatIds;

    private Integer cndOrderAmountType;

    private Double cndOrderAmountMinValue;

    private Integer cndQuantityType;

    private Integer cndQuantityMaxValue;

    private Integer cndCustomerType;

    private String cndCustomerUserIds;

    private String cndCustomerLocationIds;

    private Integer cndCouponQuantityType;

    private Integer cndCouponQuantityMaxValue;

    private Integer cndLimitedUseType;

    private Integer cndLimitedUseMaxValue;

    private Date cndValidFrom;

    private Date cndValidTo;

    private Integer cndShippingMethodType;

    private Integer cndShippingProviderType;

    private String cndShippingProviderIds;

    private Integer cndPaymentType;

    private Integer cndBuyType;

    private Boolean cndApplyMultipleCoupons;

    private StoreResponseLite storeOwner;

    private Long savedUserVoucherId;
}
