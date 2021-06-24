package vn.prostylee.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.order.dto.filter.OrderStatusMstFilter;
import vn.prostylee.order.dto.request.OrderStatusMstRequest;
import vn.prostylee.order.dto.response.OrderStatusMstResponse;
import vn.prostylee.order.service.OrderStatusMstService;
import vn.prostylee.product.dto.filter.BrandFilter;
import vn.prostylee.product.dto.request.BrandRequest;
import vn.prostylee.product.dto.response.BrandResponse;
import vn.prostylee.product.service.BrandService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/order-status")
public class OrderStatusMstController extends CrudController<OrderStatusMstRequest, OrderStatusMstResponse, Long, OrderStatusMstFilter> {

    public OrderStatusMstController(OrderStatusMstService orderStatusMstService) {
        super(orderStatusMstService);
    }
}
