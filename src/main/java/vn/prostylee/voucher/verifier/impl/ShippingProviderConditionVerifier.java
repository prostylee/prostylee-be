package vn.prostylee.voucher.verifier.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.CollectionExtUtils;
import vn.prostylee.voucher.constant.ShippingProviderType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@Order(30)
@Component
public class ShippingProviderConditionVerifier implements VoucherConditionVerifier {

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndShippingProviderType() == null
                || voucher.getCndShippingProviderType() == ShippingProviderType.NONE.getType()
                || voucher.getCndShippingProviderType() == ShippingProviderType.ALL.getType()
        ) {
            return true;
        }
        if (request.getOrder().getShippingProviderId() == null) {
            return false;
        }
        return CollectionExtUtils.contains(voucher.getCndShippingProviderIds(), request.getOrder().getShippingProviderId());
    }
}
