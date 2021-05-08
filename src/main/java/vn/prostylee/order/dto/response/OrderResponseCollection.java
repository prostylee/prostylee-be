package vn.prostylee.order.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderResponseCollection implements Serializable {

    private List<OrderResponse> orders;

}
