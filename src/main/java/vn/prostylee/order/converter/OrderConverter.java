package vn.prostylee.order.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.order.constants.OrderStatus;
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
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.store.entity.Branch;
import vn.prostylee.store.entity.Store;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class OrderConverter {

    public void toEntity(OrderRequest request, Order order) {
        Optional.ofNullable(request.getPaymentTypeId())
                .ifPresent(paymentTypeId -> {
                    PaymentType paymentType = PaymentType.builder().id(paymentTypeId).build();
                    order.setPaymentType(paymentType);
                });

        Optional.ofNullable(request.getShippingProviderId())
                .ifPresent(shippingProviderId -> {
                    ShippingProvider sp = ShippingProvider.builder().id(shippingProviderId).build();
                    order.setShippingProvider(sp);
                });

        Optional.ofNullable(request.getShippingAddress())
                .ifPresent(shippingAddressRequest -> {
                    ShippingAddress sa = BeanUtil.copyProperties(shippingAddressRequest, ShippingAddress.class);
                    order.setShippingAddress(sa);
                });

        order.setStatus(OrderStatus.getByStatusValue(request.getStatus()));
        convertOrderDetails(request, order);
        convertOrderDiscounts(request, order);
    }

    private void convertOrderDetails(OrderRequest request, Order order) {
        Set<OrderDetail> details = Optional.ofNullable(request.getOrderDetails())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(detailRq -> {
                    OrderDetail detail = BeanUtil.copyProperties(detailRq, OrderDetail.class);
                    detail.setOrder(order);

                    Product product = Product.builder().id(detailRq.getProductId()).build();
                    detail.setProduct(product);

                    Optional.ofNullable(detailRq.getStoreId()).ifPresent(storeId -> {
                        Store store = Store.builder().id(storeId).build();
                        detail.setStore(store);
                    });

                    Optional.ofNullable(detailRq.getBranchId()).ifPresent(branchId -> {
                        Branch branch = Branch.builder().id(detailRq.getBranchId()).build();
                        detail.setBranch(branch);
                    });

                    return detail;
                })
                .collect(Collectors.toSet());

        Set<OrderDetail> entityDetails = order.getOrderDetails();
        if (entityDetails == null) {
            order.setOrderDetails(details);
            return;
        }
        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(details);
    }

    private void convertOrderDiscounts(OrderRequest request, Order order) {
        Set<OrderDiscount> discounts = Optional.ofNullable(request.getOrderDiscounts())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(discountRq -> {
                    OrderDiscount discount = BeanUtil.copyProperties(discountRq, OrderDiscount.class);
                    discount.setOrder(order);
                    return discount;
                })
                .collect(Collectors.toSet());

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
                        .stream()
                        .map(this::convertToOrderDetailResponse)
                        .collect(Collectors.toList())
        );

        orderResponse.setOrderDiscounts(
                Optional.ofNullable(order.getOrderDiscounts())
                        .orElseGet(HashSet::new)
                        .stream()
                        .map(discount -> BeanUtil.copyProperties(discount, OrderDiscountResponse.class))
                        .collect(Collectors.toList())
        );

        orderResponse.setPaymentType(order.getPaymentType() == null ? null : order.getPaymentType().getName());
        orderResponse.setStatus(order.getStatus() == null ? null : order.getStatus().name());

        if (order.getShippingAddress() != null) {
            orderResponse.setShippingAddress(BeanUtil.copyProperties(order.getShippingAddress(), ShippingAddressResponse.class));
        }

        if (order.getShippingProvider() != null) {
            orderResponse.setShippingProvider(BeanUtil.copyProperties(order.getShippingProvider(), ShippingProviderResponse.class));
        }

        return orderResponse;
    }

    private OrderDetailResponse convertToOrderDetailResponse(OrderDetail detail) {
        OrderDetailResponse detailResponse = BeanUtil.copyProperties(detail, OrderDetailResponse.class);

        Optional.ofNullable(detail.getStore())
                .ifPresent(store -> {
                    StoreResponseLite storeResponse = BeanUtil.copyProperties(store, StoreResponseLite.class);
                    detailResponse.setStore(storeResponse);
                });

        Optional.ofNullable(detail.getBranch())
                .ifPresent(branch -> {
                    BranchResponse branchResponse = BeanUtil.copyProperties(branch, BranchResponse.class);
                    detailResponse.setBranch(branchResponse);
                });

        return detailResponse;
    }
}
