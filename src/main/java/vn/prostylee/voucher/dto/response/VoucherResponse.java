package vn.prostylee.voucher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherResponse extends AuditEntity {

    private Long id;

    private Long storeId;

    private String name;

    private String description;

    private String code;

    private Boolean active;

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

    private Date deletedAt;
}
