package vn.prostylee.order.dto.response;

import lombok.Data;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {

    @NotNull
    private String code;

    @NotNull
    private Date createdAt;

    private Integer status;

    @NotNull
    @Min(1)
    private Double totalMoney;

    @NotNull
    private Long buyerId;

    private List<OrderDetailResponse> orderDetails;

    private List<OrderDiscountResponse> orderDiscounts;

    private String paymentType;

    @NotNull
    private ShippingAddressResponse shippingAddress;

    @NotNull
    private ShippingProviderResponse shippingProvider;

}
