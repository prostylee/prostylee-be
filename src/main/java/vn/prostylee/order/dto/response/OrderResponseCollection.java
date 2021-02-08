package vn.prostylee.order.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseCollection {

    private List<OrderResponse> orders;

}
