package vn.prostylee.order.controller;

import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.request.OrderStatusRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.service.OrderService;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/orders")
public class OrderController extends CrudController<OrderRequest, OrderResponse, Long, OrderFilter> {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        super(orderService);
        this.orderService = orderService;
    }

    @PatchMapping("{id}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @Valid @RequestBody OrderStatusRequest statusRequest) {
        return orderService.updateStatus(id, statusRequest);
    }

    @PatchMapping("/reOder/{id}")
    public OrderResponse reOrder(@PathVariable Long id){
        return orderService.reOrder(id);
    }
}
