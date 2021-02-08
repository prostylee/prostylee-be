package vn.prostylee.order.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.dto.response.OrderResponseCollection;

public interface OrderService extends CrudService<OrderRequest, OrderResponse, Long> {

    OrderResponseCollection getOrdersByStatus(Integer statusId);

    OrderResponse updateStatus(Long id, Integer statusId);
}
