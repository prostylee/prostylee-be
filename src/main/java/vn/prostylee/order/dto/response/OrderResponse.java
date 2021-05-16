package vn.prostylee.order.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.core.dto.response.AuditResponse;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderResponse extends AuditResponse {

    private Long id;

    private String code;

    private Date createdAt;

    private String status;

    private Double totalMoney;

    private Long buyerId;

    private UserResponse buyer;

    private List<OrderDetailResponse> orderDetails;

    private List<OrderDiscountResponse> orderDiscounts;

    private String paymentType;

    private ShippingAddressResponse shippingAddress;

    private ShippingProviderResponse shippingProvider;

}
