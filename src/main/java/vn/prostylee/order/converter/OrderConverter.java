package vn.prostylee.order.converter;

import org.springframework.stereotype.Component;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.order.constants.OrderStatus;
import vn.prostylee.order.dto.request.OrderDetailRequest;
import vn.prostylee.order.dto.request.OrderDiscountRequest;
import vn.prostylee.order.dto.request.OrderRequest;
import vn.prostylee.order.dto.response.OrderDetailResponse;
import vn.prostylee.order.dto.response.OrderDiscountResponse;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.entity.Order;
import vn.prostylee.order.entity.OrderDetail;
import vn.prostylee.order.entity.OrderDiscount;
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
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.repository.StoreRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    private final PaymentTypeRepository paymentTypeRepository;

    private final ShippingProviderRepository shippingProviderRepository;

    private final ShippingAddressRepository shippingAddressRepository;

    private final ProductRepository productRepository;

    private final StoreRepository storeRepository;

    private static Map<Long, Product> productCache;

    private static Map<Long, Store> storeCache;

    public OrderConverter(PaymentTypeRepository paymentTypeRepository,
                          ShippingProviderRepository shippingProviderRepository,
                          ShippingAddressRepository shippingAddressRepository,
                          ProductRepository productRepository,
                          StoreRepository storeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.shippingProviderRepository = shippingProviderRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        productCache = new HashMap<>();
        storeCache = new HashMap<>();
    }

    public void convertRequestToEntity(OrderRequest request, Order order) {
        PaymentType paymentType = getPaymentById(request.getPaymentTypeId());
        order.setPaymentType(paymentType);
        ShippingProvider sp = getShippingProviderById(request.getShippingProviderId());
        order.setShippingProvider(sp);
        ShippingAddress sa = getShippingAddressById(request.getShippingAddressId());
        order.setShippingAddress(sa);
        order.setStatus(OrderStatus.getByStatusValue(request.getStatus()));

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
        Set<OrderDetail> entityDetails = order.getOrderDetails();
        if (entityDetails == null) {
            order.setOrderDetails(details);
            return;
        }
        entityDetails.clear();
        entityDetails.addAll(details);
    }

    private void convertOrderDiscounts(OrderRequest request, Order order) {
        Set<OrderDiscount> discounts = new HashSet<>();
        for (OrderDiscountRequest discountRq : request.getOrderDiscounts()) {
            OrderDiscount discount = BeanUtil.copyProperties(discountRq, OrderDiscount.class);
            discount.setOrder(order);
            discounts.add(discount);
        }
        Set<OrderDiscount> entityDiscounts = order.getOrderDiscounts();
        if (entityDiscounts == null) {
            order.setOrderDiscounts(discounts);
            return;
        }
        order.getOrderDiscounts().clear();
        order.getOrderDiscounts().addAll(discounts);
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

    public OrderResponse convertToResponse(Order order) {
        OrderResponse orderResponse = BeanUtil.copyProperties(order, OrderResponse.class);
        orderResponse.setOrderDetails(
                Optional.ofNullable(order.getOrderDetails())
                        .orElseGet(HashSet::new)
                        .stream().map(this::convertToOrderDetailResponse)
                        .collect(Collectors.toList())
        );
        orderResponse.setOrderDiscounts(
                Optional.ofNullable(order.getOrderDiscounts())
                        .orElseGet(HashSet::new)
                        .stream().map(discount -> BeanUtil.copyProperties(discount, OrderDiscountResponse.class))
                        .collect(Collectors.toList())
        );
        orderResponse.setPaymentType(order.getPaymentType().getName());
        orderResponse.setStatus(order.getStatus().name());
        orderResponse.setShippingAddress(BeanUtil.copyProperties(order.getShippingAddress(), ShippingAddressResponse.class));
        orderResponse.setShippingProvider(BeanUtil.copyProperties(order.getShippingProvider(), ShippingProviderResponse.class));
        return orderResponse;
    }

    private OrderDetailResponse convertToOrderDetailResponse(OrderDetail detail) {
        OrderDetailResponse detailResponse = BeanUtil.copyProperties(detail, OrderDetailResponse.class);
        StoreResponseLite storeResponse = BeanUtil.copyProperties(detail.getStore(), StoreResponseLite.class);
        detailResponse.setStore(storeResponse);
        return detailResponse;
    }
}
