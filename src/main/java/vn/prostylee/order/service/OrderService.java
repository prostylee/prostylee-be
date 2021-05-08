package vn.prostylee.order.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.request.OrderStatusRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.dto.response.ProductSoldCountResponse;

import java.util.List;

public interface OrderService extends CrudService<OrderRequest, OrderResponse, Long> {

    OrderResponse updateStatus(Long id, OrderStatusRequest statusRequest);

    List<Long> getBestSellerProductIds(BestSellerFilter bestSellerFilter);

    Page<ProductSoldCountResponse> countProductSold(PagingParam pagingParam);

    Page<Long> getPurchasedProductIdsByUserId(Long userId, PagingParam pagingParam);

    OrderResponse reOrder(Long id);
}
