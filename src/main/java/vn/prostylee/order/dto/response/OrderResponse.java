package vn.prostylee.order.dto.response;

import lombok.Data;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private String code;

    private Date createdAt;

    private String status;

    private Double totalMoney;

    private Long buyerId;

    private List<OrderDetailResponse> orderDetails;

    private List<OrderDiscountResponse> orderDiscounts;

    private String paymentType;

    private ShippingAddressResponse shippingAddress;

    private ShippingProviderResponse shippingProvider;

}
