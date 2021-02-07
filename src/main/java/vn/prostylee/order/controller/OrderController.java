package vn.prostylee.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.service.OrderService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/orders")
public class OrderController extends CrudController<OrderRequest, OrderResponse, Long, OrderFilter> {

    public OrderController(OrderService orderService) {
        super(orderService);
    }
}
