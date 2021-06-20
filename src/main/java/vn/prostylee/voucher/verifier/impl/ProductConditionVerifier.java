package vn.prostylee.voucher.verifier.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.CollectionExtUtils;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.voucher.constant.ProductType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Order(100)
@Component
public class ProductConditionVerifier implements VoucherConditionVerifier {

    private final ProductService productService;

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndProductType() == null) {
            return true;
        }

        ProductType type = ProductType.findByProductType(voucher.getCndProductType()).orElse(null);
        if (type == null) {
            return true;
        }

        switch (type) {
            case SPECIFIC_PRODUCT:
                return verifyBySpecificProduct(voucher, request);

            case SPECIFIC_CATEGORY:
                return verifyBySpecificCategory(voucher, request);

            case ALL:
            case NONE:
            default:
                return true;
        }
    }

    private boolean verifyBySpecificProduct(Voucher voucher, VoucherCalculationRequest request) {
        if (CollectionUtils.isEmpty(voucher.getCndProductProdIds())) {
            return false;
        }

        List<Long> productIds = request.getProductIds();
        return productIds.stream().anyMatch(productId -> CollectionExtUtils.contains(voucher.getCndProductProdIds(), productId));
    }

    private boolean verifyBySpecificCategory(Voucher voucher, VoucherCalculationRequest request) {
        if (CollectionUtils.isEmpty(voucher.getCndProductCatIds())) {
            return false;
        }

        List<Long> productIds = request.getProductIds();
        return productService.findByIds(productIds)
                .stream()
                .map(ProductResponseLite::getCategoryId)
                .filter(Objects::nonNull)
                .anyMatch(categoryId -> CollectionExtUtils.contains(voucher.getCndProductCatIds(), categoryId));
    }
}
