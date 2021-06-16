package vn.prostylee.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.order.dto.request.OrderDetailAttributeRequest;
import vn.prostylee.order.dto.response.OrderDetailAttributeResponse;
import vn.prostylee.order.entity.OrderDetailAttribute;
import vn.prostylee.order.repository.OrderDetailAttributeRepository;
import vn.prostylee.order.service.OrderDetailAttributeService;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderDetailAttributeServiceImpl implements OrderDetailAttributeService {

    private final OrderDetailAttributeRepository orderDetailAttributeRepository;

    @Override
    public Page<OrderDetailAttributeResponse> findAll(BaseFilter baseFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OrderDetailAttributeResponse findById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OrderDetailAttributeResponse save(OrderDetailAttributeRequest request) {
        OrderDetailAttribute entity = BeanUtil.copyProperties(request, OrderDetailAttribute.class);
        OrderDetailAttribute savedEntity = orderDetailAttributeRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, OrderDetailAttributeResponse.class);
    }

    @Override
    public OrderDetailAttributeResponse update(Long aLong, OrderDetailAttributeRequest s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
