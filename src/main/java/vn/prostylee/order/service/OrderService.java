package vn.prostylee.order.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.request.OrderStatusRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;

import java.util.List;

public interface OrderService extends CrudService<OrderRequest, OrderResponse, Long> {

    OrderResponse updateStatus(Long id, OrderStatusRequest statusRequest);

    List<Long> getBestSellerProductIds(BestSellerFilter bestSellerFilter);

    List<Long> getPaidStores(PaidStoreRequest paidStoreRequest);
}
