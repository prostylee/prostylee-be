package vn.prostylee.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.order.dto.request.OrderDetailRequest;
import vn.prostylee.order.dto.request.OrderDiscountRequest;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderDetailResponse;
import vn.prostylee.order.dto.response.OrderDiscountResponse;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.dto.response.OrderResponseCollection;
import vn.prostylee.order.entity.Order;
import vn.prostylee.order.entity.OrderDetail;
import vn.prostylee.order.entity.OrderDiscount;
import vn.prostylee.order.repository.OrderRepository;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.payment.entity.PaymentType;
import vn.prostylee.payment.repository.PaymentTypeRepository;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingAddress;
import vn.prostylee.shipping.entity.ShippingProvider;
import vn.prostylee.shipping.repository.ShippingAddressRepository;
import vn.prostylee.shipping.repository.ShippingProviderRepository;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.repository.StoreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final PaymentTypeRepository paymentTypeRepository;

    private final ShippingProviderRepository shippingProviderRepository;

    private final ShippingAddressRepository shippingAddressRepository;

    private final ProductRepository productRepository;

    private final StoreRepository storeRepository;

    private final BaseFilterSpecs<Order> baseFilterSpecs;

    private static Map<Long, Product> productCache;

    private static Map<Long, Store> storeCache;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentTypeRepository paymentTypeRepository,
                            ShippingProviderRepository shippingProviderRepository,
                            ShippingAddressRepository shippingAddressRepository,
                            ProductRepository productRepository,
                            StoreRepository storeRepository,
                            BaseFilterSpecs<Order> baseFilterSpecs) {
        this.orderRepository = orderRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.shippingProviderRepository = shippingProviderRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.baseFilterSpecs = baseFilterSpecs;
        productCache = new HashMap<>();
        storeCache = new HashMap<>();
    }

    @Override
    public Page<OrderResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = getOrderById(id);
        return convertToResponse(order);
    }

    @Override
    public OrderResponse save(OrderRequest request) {
        Order order = BeanUtil.copyProperties(request, Order.class);
        convertRequestToEntity(request, order);
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    private void convertRequestToEntity(OrderRequest request, Order order) {
        PaymentType paymentType = getPaymentById(request.getPaymentTypeId());
        order.setPaymentType(paymentType);
        ShippingProvider sp = getShippingProviderById(request.getShippingProviderId());
        order.setShippingProvider(sp);
        ShippingAddress sa = getShippingAddressById(request.getShippingAddressId());
        order.setShippingAddress(sa);

        convertOrderDetails(request, order);
        convertOrderDiscounts(request, order);
    }

    private void convertOrderDetails(OrderRequest request, Order order) {
        Set<OrderDetail> details = new HashSet<>();
        for(OrderDetailRequest detailRq : request.getOrderDetails()) {
            OrderDetail detail = BeanUtil.copyProperties(detailRq, OrderDetail.class);
            detail.setOrder(order);
            Product product = getProductById(detailRq.getProductId());
            detail.setProduct(product);
            Store store = getStoreById(detailRq.getStoreId());
            detail.setStore(store);
            details.add(detail);
        }
        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(details);
    }

    private void convertOrderDiscounts(OrderRequest request, Order order) {
        Set<OrderDiscount> discounts = new HashSet<>();
        for (OrderDiscountRequest discountRq : request.getOrderDiscounts()) {
            OrderDiscount discount = BeanUtil.copyProperties(discountRq, OrderDiscount.class);
            discount.setOrder(order);
            discounts.add(discount);
        }
        order.getOrderDiscounts().clear();
        order.getOrderDiscounts().addAll(discounts);
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        Order order = getOrderById(id);
        BeanUtil.mergeProperties(request, order);
        convertRequestToEntity(request, order);
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found with id [" + id + "]"));
    }

    private PaymentType getPaymentById(Long id) {
        return paymentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment type is not found with id [" + id + "]"));
    }

    private ShippingProvider getShippingProviderById(Long id) {
        return shippingProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping provider is not found with id [" + id + "]"));
    }

    private ShippingAddress getShippingAddressById(Long id) {
        return shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address is not found with id [" + id + "]"));
    }

    private Product getProductById(Long id) {
        Product product = productCache.get(id);
        if (product == null) {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product is not found with id [" + id + "]"));
            productCache.put(id, product);
        }
        return product;
    }

    private Store getStoreById(Long id) {
        Store store = storeCache.get(id);
        if (store == null) {
            store = storeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Store is not found with id [" + id + "]"));
            storeCache.put(id, store);
        }
        return store;
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse orderResponse = BeanUtil.copyProperties(order, OrderResponse.class);
        orderResponse.setOrderDetails(
                Optional.ofNullable(order.getOrderDetails())
                    .orElseGet(HashSet::new)
                    .stream().map(detail -> BeanUtil.copyProperties(detail, OrderDetailResponse.class))
                    .collect(Collectors.toList())
        );
        orderResponse.setOrderDiscounts(
                Optional.ofNullable(order.getOrderDiscounts())
                        .orElseGet(HashSet::new)
                        .stream().map(discount -> BeanUtil.copyProperties(discount, OrderDiscountResponse.class))
                        .collect(Collectors.toList())
        );
        orderResponse.setShippingAddress(BeanUtil.copyProperties(order.getShippingAddress(), ShippingAddressResponse.class));
        orderResponse.setShippingProvider(BeanUtil.copyProperties(order.getShippingProvider(), ShippingProviderResponse.class));
        return orderResponse;
    }

    @Override
    public OrderResponseCollection getOrdersByStatus(Integer statusId) {
        List<Order> orders = orderRepository.findAllByStatus(statusId);
        List<OrderResponse> ordersResponse = Optional.ofNullable(orders)
                .orElseGet(ArrayList::new)
                .stream().map(this::convertToResponse)
                .collect(Collectors.toList());
        OrderResponseCollection response = new OrderResponseCollection();
        response.setOrders(ordersResponse);
        return response;
    }


    @Override
    public OrderResponse updateStatus(Long id, Integer statusId) {
        Order order = getOrderById(id);
        order.setStatus(statusId);
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }
}
