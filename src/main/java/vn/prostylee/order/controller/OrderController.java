package vn.prostylee.order.controller;

import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.dto.response.OrderResponseCollection;
import vn.prostylee.order.service.OrderService;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/orders")
public class OrderController extends CrudController<OrderRequest, OrderResponse, Long, OrderFilter> {

    public OrderService orderService;

    public OrderController(OrderService orderService) {
        super(orderService);
        this.orderService = orderService;
    }

    @GetMapping("status/{statusId}")
    public OrderResponseCollection getStatus(@Min(0) @PathVariable Integer statusId) {
        return orderService.getOrdersByStatus(statusId);
    }

    @PutMapping("{id}/status/{statusId}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @Min(0) @PathVariable Integer statusId) {
        return orderService.updateStatus(id, statusId);
    }

}
