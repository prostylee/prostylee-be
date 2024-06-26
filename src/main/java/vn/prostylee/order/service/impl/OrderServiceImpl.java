package vn.prostylee.order.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.configuration.monitor.workflow.MutationAction;
import vn.prostylee.core.configuration.monitor.workflow.MutationEventListener;
import vn.prostylee.core.configuration.monitor.workflow.MutationPhase;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.specs.QueryBuilder;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.order.constants.OrderHistoryStatus;
import vn.prostylee.order.constants.OrderStatus;
import vn.prostylee.order.converter.OrderConverter;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.request.OrderAtStoreRequest;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.request.OrderStatusRequest;
import vn.prostylee.order.dto.response.OrderAtStoreResponse;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.dto.response.ProductOrderResponse;
import vn.prostylee.order.dto.response.ProductSoldCountResponse;
import vn.prostylee.order.entity.Order;
import vn.prostylee.order.entity.OrderDetail;
import vn.prostylee.order.entity.OrderDiscount;
import vn.prostylee.order.entity.OrderHistory;
import vn.prostylee.order.handler.OrderAtStoreMutationEventHandler;
import vn.prostylee.order.handler.OrderMutationEventHandler;
import vn.prostylee.order.repository.OrderDetailRepository;
import vn.prostylee.order.repository.OrderRepository;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.response.StoreResponseLite;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static vn.prostylee.order.constants.OrderStatus.CANCEL_ORDER;
import static vn.prostylee.order.constants.OrderStatus.CREATE_ORDER;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderConverter orderConverter;
    private final BaseFilterSpecs<Order> baseFilterSpecs;

    @Override
    public Page<OrderResponse> findAll(BaseFilter filter) {
        OrderFilter orderFilter = (OrderFilter) filter;
        Specification<Order> mainSpec = (root, query, cb) -> {
            QueryBuilder<Order> queryBuilder = new QueryBuilder<>(cb, root);
            findByStatus(orderFilter, queryBuilder);
            findByLoggedInUser(orderFilter, queryBuilder);
            Predicate[] orPredicates = queryBuilder.build();
            return cb.and(orPredicates);
        };
        if (StringUtils.isNotBlank(orderFilter.getKeyword())) {
            Specification<Order> searchSpec = baseFilterSpecs.search(orderFilter);
            mainSpec = mainSpec.and(searchSpec);
        }
        Pageable pageable = baseFilterSpecs.page(orderFilter);
        Page<Order> orders = orderRepository.findAll(mainSpec, pageable);
        return orders.map(orderConverter::toDto);
    }

    private void findByStatus(OrderFilter orderFilter, QueryBuilder<Order> queryBuilder) {
        if(orderFilter.getStatusId() == null) {
            return;
        }
        Long statusId = orderFilter.getStatusId();
        queryBuilder.equals("statusId", statusId);
    }

    private void findByLoggedInUser(OrderFilter orderFilter, QueryBuilder<Order> queryBuilder) {
        queryBuilder.equals("createdBy", orderFilter.getLoggedInUser());
    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = getOrderById(id);
        return orderConverter.toDto(order);
    }

    @MutationEventListener(handler = OrderMutationEventHandler.class, action = MutationAction.CREATE, phase = MutationPhase.AFTER)
    @Override
    public OrderResponse save(OrderRequest request) {
        Order order = BeanUtil.copyProperties(request, Order.class);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        order.setCode(sdf.format(cal.getTime()));
        orderConverter.toEntity(request, order);
        Order savedOrder = orderRepository.save(order);
        return orderConverter.toDto(savedOrder);
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        Order order = getOrderById(id);
        BeanUtil.mergeProperties(request, order);
        orderConverter.toEntity(request, order);
        Order savedOrder = orderRepository.save(order);
        return orderConverter.toDto(savedOrder);
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException();
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found with id [" + id + "]"));
    }

    @Override
    public OrderResponse updateStatus(Long id, OrderStatusRequest statusRequest) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.getByStatusValue(statusRequest.getAct()));
        Set<OrderHistory> orderHistoryList = Optional.ofNullable(order.getOrderHistories()).orElseGet(HashSet::new)
                .stream()
                .map(object -> {
                    if (order.getStatus().getStatus() == object.getActCode()){
                        object.setCompletedStatus(OrderHistoryStatus.AWAITING_CONFIRMATION.getCompletedStatus());
                        order.setStatusId(object.getStatusId());
                    }
                    OrderHistory response = BeanUtil.copyProperties(object,OrderHistory.class);
                    return response;
                }).collect(Collectors.toSet());
        order.getOrderHistories().clear();
        order.getOrderHistories().addAll(orderHistoryList);
        Order savedOrder = orderRepository.save(order);
        return orderConverter.toDto(savedOrder);
    }

    @Override
    public List<Long> getBestSellerProductIds(BestSellerFilter bestSellerFilter) {
        Date fromDate = DateUtils.getLastDaysBefore(bestSellerFilter.getTimeRangeInDays());
        Date toDate = Calendar.getInstance().getTime();
        Pageable pageSpecification = PageRequest.of(bestSellerFilter.getPage(), bestSellerFilter.getLimit());
        return orderDetailRepository.getBestSellerProductIds(bestSellerFilter.getStoreId(), fromDate, toDate, pageSpecification);
    }

    @Override
    public OrderResponse reOrder(Long id){
        Order newOrder = this.getOrderById(id);
        if(newOrder == null){
            return null;
        }
        newOrder.setId(null);
        //Set orderdetail
        Set<OrderDetail> newOrderDetail = new HashSet<>();
        for (OrderDetail item: newOrder.getOrderDetails()
             ) {
            OrderDetail newDetail = BeanUtil.copyProperties(item,OrderDetail.class);
            newDetail.setId(null);
            newDetail.setOrder(newOrder);
            newOrderDetail.add(newDetail);
        }
        newOrder.getOrderDetails().clear();
        newOrder.setOrderDetails(newOrderDetail);
        //Set discount
        Set<OrderDiscount> newOrderDiscount = new HashSet<>();
        for (OrderDiscount item: newOrder.getOrderDiscounts()
             ) {
            OrderDiscount newDiscount = BeanUtil.copyProperties(item,OrderDiscount.class);
            newDiscount.setId(null);
            newDiscount.setOrder(newOrder);
            newOrderDiscount.add(newDiscount);
        }
        newOrder.getOrderDiscounts().clear();
        newOrder.setOrderDiscounts(newOrderDiscount);
        newOrder.getOrderHistories().clear();
        newOrder.setStatus(CREATE_ORDER);
        newOrder.setStatusId(0L);
        return orderConverter.toDto(newOrder);
    }

    @Override
    public Page<ProductSoldCountResponse> countProductSold(PagingParam pagingParam) {
        Pageable pageSpecification = PageRequest.of(pagingParam.getPage(), pagingParam.getLimit());
        return orderDetailRepository.countProductSold(pageSpecification);
    }

    @Override
    public Page<Long> getPurchasedProductIdsByUserId(Long userId, PagingParam pagingParam) {
        Pageable pageSpecification = PageRequest.of(pagingParam.getPage(), pagingParam.getLimit());
        Page<ProductOrderResponse> page = orderDetailRepository.getPurchasedProductIdsByUserId(userId, pageSpecification);
        List<Long> productIds = page.stream().map(ProductOrderResponse::getProductId).collect(Collectors.toList());
        return new PageImpl<>(productIds, page.getPageable(), page.getTotalElements());
    }

    @Override
    @MutationEventListener(handler = OrderAtStoreMutationEventHandler.class, action = MutationAction.CREATE, phase = MutationPhase.AFTER)
    public OrderAtStoreResponse orderAtStore(OrderAtStoreRequest request){
        return orderConverter.convertOrderAtStore(request);
    }
}
