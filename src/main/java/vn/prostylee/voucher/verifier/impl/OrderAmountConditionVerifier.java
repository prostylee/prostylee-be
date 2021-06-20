package vn.prostylee.voucher.verifier.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.voucher.constant.OrderAmountType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderDetailRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Order(90)
@Component
public class OrderAmountConditionVerifier implements VoucherConditionVerifier {

    private final ProductService productService;

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndOrderAmountType() == null || voucher.getCndOrderAmountType() == OrderAmountType.NONE.getType()) {
            return true;
        }
        Double orderAmount = calculateAmount(request);
        return orderAmount >= voucher.getCndOrderAmountMinValue();
    }

    private Double calculateAmount(VoucherCalculationRequest request) {
        List<VoucherOrderDetailRequest> orderDetails = Optional.ofNullable(request.getOrderDetails())
                .orElseGet(Collections::emptyList);

        List<Long> productIds = request.getProductIds();

        Map<Long, ProductResponseLite> mapProducts = productService.findByIds(productIds)
                .stream()
                .collect(Collectors.toMap(ProductResponseLite::getId, prod -> prod));

        return orderDetails.stream()
                .map(orderDetail -> {
                    ProductResponseLite product = mapProducts.get(orderDetail.getProductId());
                    if (product != null) {
                        Double price = Optional.ofNullable(product.getPriceSale()).orElse(product.getPrice());
                        return orderDetail.getAmount() * price;
                    }
                    return 0.0;
                })
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
