package vn.prostylee.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.service.ShippingProviderService;
import vn.prostylee.voucher.constant.DiscountType;
import vn.prostylee.voucher.constant.OrderAmountType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderDetailRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.dto.response.VoucherOrderDetailResponse;
import vn.prostylee.voucher.dto.response.VoucherOrderResponse;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.service.VoucherCalculatorService;
import vn.prostylee.voucher.verifier.impl.ProductConditionVerifier;
import vn.prostylee.voucher.verifier.impl.ShippingProviderConditionVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VoucherCalculatorServiceImpl implements VoucherCalculatorService {

    private final ProductService productService;
    private final ShippingProviderService shippingProviderService;

    private final ProductConditionVerifier productConditionVerifier;
    private final ShippingProviderConditionVerifier shippingProviderConditionVerifier;

    @Override
    public VoucherCalculationResponse calculateDiscount(Voucher voucher, VoucherCalculationRequest request) {
        List<VoucherOrderDetailResponse> details = calculateOrderDetails(voucher, request);
        VoucherOrderResponse order = calculateOrder(voucher, request, details);

        return VoucherCalculationResponse.builder()
                .voucherId(voucher.getId())
                .code(voucher.getCode())
                .expiredDate(voucher.getCndValidTo())
                .order(order)
                .orderDetails(details)
                .build();
    }

    private VoucherOrderResponse calculateOrder(Voucher voucher, VoucherCalculationRequest request, List<VoucherOrderDetailResponse> details) {
        VoucherOrderRequest order = request.getOrder();
        Double amount = details.stream().map(VoucherOrderDetailResponse::getAmount).reduce(0.0, Double::sum);
        Double discountAmount = 0.0;

        if (productConditionVerifier.isSatisfyCondition(voucher, request)) { // Discount for products
            discountAmount = details.stream().map(VoucherOrderDetailResponse::getDiscountAmount).reduce(0.0, Double::sum);
        } else if (shippingProviderConditionVerifier.isSatisfyCondition(voucher, request)) { // Discount for delivery
            discountAmount = calculateDiscountAmountByShippingId(request.getOrder().getShippingProviderId(), voucher);
        }

        Double balance = amount - discountAmount;

        return VoucherOrderResponse.builder()
                .amount(amount)
                .discountAmount(discountAmount)
                .balance(balance)
                .buyerId(order.getBuyerId())
                .paymentTypeId(order.getPaymentTypeId())
                .shippingMethodId(order.getShippingMethodId())
                .shippingProviderId(order.getShippingProviderId())
                .build();
    }

    private Double calculateDiscountAmountByShippingId(Long shippingId, Voucher voucher) {
        Double discountAmount = voucher.getDiscountAmount();

        ShippingProviderResponse shippingProvider = shippingProviderService.findById(shippingId);
        Double shippingPricing = shippingProvider.getPrice();

        if (voucher.getType() != null && DiscountType.PERCENT.getType() == voucher.getType()) {
            discountAmount = shippingPricing * (discountAmount / 100);
        }

        if (voucher.getDiscountMaxAmount() != null && discountAmount > voucher.getDiscountMaxAmount()) {
            discountAmount = voucher.getDiscountMaxAmount();
        }

        return Math.min(shippingPricing, discountAmount);
    }

    private List<VoucherOrderDetailResponse> calculateOrderDetails(Voucher voucher, VoucherCalculationRequest request) {
        List<VoucherOrderDetailRequest> orderDetails = Optional.ofNullable(request.getOrderDetails()).orElseGet(Collections::emptyList);

        if (productConditionVerifier.isSatisfyCondition(voucher, request)) {
            return orderDetails.stream()
                    .map(detail -> calculateOrderDetailResponse(voucher, detail))
                    .collect(Collectors.toList());
        }

        return orderDetails.stream()
                .map(this::buildNonDiscountOrderDetailResponse)
                .collect(Collectors.toList());
    }

    private VoucherOrderDetailResponse buildNonDiscountOrderDetailResponse(VoucherOrderDetailRequest detailRequest) {
        ProductResponse product = productService.findById(detailRequest.getProductId());

        final Double price = Optional.ofNullable(product.getPriceSale()).orElse(product.getPrice());
        final int quantity = detailRequest.getQuantity();
        final Double total = quantity * price;

        return VoucherOrderDetailResponse.builder()
                .storeId(product.getStoreId())
                .categoryId(product.getCategoryId())
                .productId(detailRequest.getProductId())
                .quantity(detailRequest.getQuantity())
                .price(price)
                .amount(total)
                .discountAmount(0.0)
                .balance(total)
                .build();
    }

    private VoucherOrderDetailResponse calculateOrderDetailResponse(Voucher voucher, VoucherOrderDetailRequest detailRequest) {
        ProductResponse product = productService.findById(detailRequest.getProductId());

        final Double price = Optional.ofNullable(product.getPriceSale()).orElse(product.getPrice());

        int quantity = detailRequest.getQuantity();
        if (voucher.getCndOrderAmountType() != null && voucher.getCndOrderAmountType() == OrderAmountType.LIMIT_MIN.getType()) {
            quantity = Math.min(voucher.getCndQuantityMaxValue(), detailRequest.getQuantity());
        }

        final Double total = quantity * price;

        Double discount = voucher.getDiscountAmount();
        if (voucher.getType() != null && DiscountType.PERCENT.getType() == voucher.getType()) {
            discount = total * (discount / 100);
        }
        if (voucher.getDiscountMaxAmount() != null && discount > voucher.getDiscountMaxAmount()) {
            discount = voucher.getDiscountMaxAmount();
        }

        final Double balance = total - discount;

        return VoucherOrderDetailResponse.builder()
                .storeId(product.getStoreId())
                .categoryId(product.getCategoryId())
                .productId(detailRequest.getProductId())
                .quantity(detailRequest.getQuantity())
                .price(price)
                .amount(total)
                .discountAmount(discount)
                .balance(balance)
                .build();
    }
}
