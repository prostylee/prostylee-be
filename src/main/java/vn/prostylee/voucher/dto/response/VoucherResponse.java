package vn.prostylee.voucher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;

import java.util.Date;
import java.util.List;

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

    private List<Long> cndProductProdIds;

    private List<Long> cndProductCatIds;

    private Integer cndOrderAmountType;

    private Double cndOrderAmountMinValue;

    private Integer cndQuantityType;

    private Integer cndQuantityMaxValue;

    private Integer cndCustomerType;

    private List<Long> cndCustomerUserIds;

    private List<Long> cndCustomerAddressIds;

    private Integer cndCouponQuantityType;

    private Integer cndCouponQuantityMaxValue;

    private Integer cndLimitedUseType;

    private Integer cndLimitedUseMaxValue;

    private Date cndValidFrom;

    private Date cndValidTo;

    private Integer cndShippingMethodType;

    private List<Long> cndShippingMethodIds;

    private Integer cndShippingProviderType;

    private List<Long> cndShippingProviderIds;

    private Integer cndPaymentType;

    private Integer cndBuyType;

    private Boolean cndApplyMultipleCoupons;

    private Date deletedAt;
}
