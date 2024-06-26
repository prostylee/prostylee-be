package vn.prostylee.voucher.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.converter.JsonStringToListLongConverter;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Voucher generated by hbm2java
 *
 * @author prostylee
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "voucher")
public class Voucher extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "voucher_seq", sequenceName = "voucher_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voucher_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "type")
    private Integer type;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "discount_max_amount")
    private Double discountMaxAmount;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "cnd_product_type")
    private Integer cndProductType;

    @Column(name = "cnd_product_prod_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndProductProdIds;

    @Column(name = "cnd_product_cat_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndProductCatIds;

    @Column(name = "cnd_order_amount_type")
    private Integer cndOrderAmountType;

    @Column(name = "cnd_order_amount_min_value")
    private Double cndOrderAmountMinValue;

    @Column(name = "cnd_quantity_type")
    private Integer cndQuantityType;

    @Column(name = "cnd_quantity_max_value")
    private Integer cndQuantityMaxValue;

    @Column(name = "cnd_customer_type")
    private Integer cndCustomerType;

    @Column(name = "cnd_customer_user_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndCustomerUserIds;

    @Column(name = "cnd_customer_address_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndCustomerAddressIds;

    @Column(name = "cnd_coupon_quantity_type")
    private Integer cndCouponQuantityType;

    @Column(name = "cnd_coupon_quantity_max_value")
    private Integer cndCouponQuantityMaxValue;

    @Column(name = "cnd_limited_use_type")
    private Integer cndLimitedUseType;

    @Column(name = "cnd_limited_use_max_value")
    private Integer cndLimitedUseMaxValue;

    @Column(name = "cnd_valid_from")
    private Date cndValidFrom;

    @Column(name = "cnd_valid_to")
    private Date cndValidTo;

    @Column(name = "cnd_shipping_method_type")
    private Integer cndShippingMethodType;

    @Column(name = "cnd_shipping_method_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndShippingMethodIds;

    @Column(name = "cnd_shipping_provider_type")
    private Integer cndShippingProviderType;

    @Column(name = "cnd_shipping_provider_ids", length = 2048)
    @Convert(converter = JsonStringToListLongConverter.class)
    private List<Long> cndShippingProviderIds;

    @Column(name = "cnd_payment_type")
    private Integer cndPaymentType;

    @Column(name = "cnd_buy_type")
    private Integer cndBuyType;

    @Column(name = "cnd_apply_multiple_coupons")
    private Boolean cndApplyMultipleCoupons;

    @Column(name = "deleted_at")
    private Date deletedAt;

}
