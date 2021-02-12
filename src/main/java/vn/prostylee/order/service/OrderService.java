package vn.prostylee.order.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.request.OrderStatusRequest;
import vn.prostylee.order.dto.response.OrderResponse;

public interface OrderService extends CrudService<OrderRequest, OrderResponse, Long> {

    OrderResponse updateStatus(Long id, OrderStatusRequest statusRequest);
}
