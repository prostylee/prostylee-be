package vn.prostylee.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.entity.Order;
import vn.prostylee.order.repository.OrderRepository;
import vn.prostylee.order.service.OrderService;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final BaseFilterSpecs<Order> baseFilterSpecs;

    @Override
    public Page<OrderResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public OrderResponse findById(Long aLong) {
        return null;
    }

    @Override
    public OrderResponse save(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse update(Long aLong, OrderRequest s) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
