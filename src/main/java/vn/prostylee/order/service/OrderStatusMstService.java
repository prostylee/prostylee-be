package vn.prostylee.order.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.order.dto.request.OrderStatusMstRequest;
import vn.prostylee.order.dto.response.OrderStatusMstResponse;

public interface OrderStatusMstService extends CrudService<OrderStatusMstRequest, OrderStatusMstResponse, Long> {
}
