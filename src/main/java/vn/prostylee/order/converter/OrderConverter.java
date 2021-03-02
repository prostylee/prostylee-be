package vn.prostylee.order.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
import vn.prostylee.product.entity.Product;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingAddress;
import vn.prostylee.shipping.entity.ShippingProvider;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.store.entity.Store;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class OrderConverter {

    public void toEntity(OrderRequest request, Order order) {
        PaymentType paymentType = PaymentType.builder().id(request.getPaymentTypeId()).build();
        order.setPaymentType(paymentType);
        ShippingProvider sp = ShippingProvider.builder().id(request.getShippingProviderId()).build();
        order.setShippingProvider(sp);
        ShippingAddress sa = ShippingAddress.builder().id(request.getShippingAddressId()).build();
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
            Product product = Product.builder().id(detailRq.getProductId()).build();
            detail.setProduct(product);
            Store store = Store.builder().id(detailRq.getStoreId()).build();
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

    public OrderResponse toDto(Order order) {
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
