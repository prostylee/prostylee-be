package vn.prostylee.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.order.dto.filter.OrderStatusMstFilter;
import vn.prostylee.order.dto.request.OrderStatusMstRequest;
import vn.prostylee.order.dto.response.OrderStatusMstResponse;
import vn.prostylee.order.entity.OrderStatusMst;
import vn.prostylee.order.repository.OrderStatusMstRepository;
import vn.prostylee.order.service.OrderStatusMstService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusMstServiceImpl implements OrderStatusMstService {
    private final OrderStatusMstRepository orderStatusMstRepository;
    private final BaseFilterSpecs<OrderStatusMst> baseFilterSpecs;

    @Override
    public Page<OrderStatusMstResponse> findAll(BaseFilter baseFilter) {
        OrderStatusMstFilter orderStatusMstFilter = (OrderStatusMstFilter) baseFilter;
        Specification<OrderStatusMst> searchable = baseFilterSpecs.search(orderStatusMstFilter);
        Pageable pageable = baseFilterSpecs.page(orderStatusMstFilter);
        Page<OrderStatusMst> page = orderStatusMstRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, OrderStatusMstResponse.class));
    }

    @Override
    public OrderStatusMstResponse findById(Long id) {
        OrderStatusMst orderStatusMst = getById(id);
        return BeanUtil.copyProperties(orderStatusMst, OrderStatusMstResponse.class);
    }

    @Override
    public OrderStatusMstResponse save(OrderStatusMstRequest orderStatusMstRequest) {
        OrderStatusMst entity = BeanUtil.copyProperties(orderStatusMstRequest, OrderStatusMst.class);
        OrderStatusMst savedEntity = orderStatusMstRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, OrderStatusMstResponse.class);
    }

    @Override
    public OrderStatusMstResponse update(Long id, OrderStatusMstRequest request) {
        OrderStatusMst entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        OrderStatusMst savedEntity = orderStatusMstRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, OrderStatusMstResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            orderStatusMstRepository.deleteById(id);
            log.info("Order Status with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Order Status id [{}] does not exists", id);
            return false;
        }
    }

    private OrderStatusMst getById(Long id) {
        return orderStatusMstRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order Status is not found with id [" + id + "]"));
    }
}
