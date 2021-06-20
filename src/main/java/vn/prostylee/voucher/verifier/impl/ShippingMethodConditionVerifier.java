package vn.prostylee.voucher.verifier.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.CollectionExtUtils;
import vn.prostylee.voucher.constant.ShippingMethodType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@Order(20)
@Component
public class ShippingMethodConditionVerifier implements VoucherConditionVerifier {

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndShippingMethodType() == null
                || voucher.getCndShippingMethodType() == ShippingMethodType.NONE.getType()
                || voucher.getCndShippingMethodType() == ShippingMethodType.ALL.getType()
        ) {
            return true;
        }
        if (request.getOrder().getShippingMethodId() == null) {
            return false;
        }
        return CollectionExtUtils.contains(voucher.getCndShippingMethodIds(), request.getOrder().getShippingMethodId());
    }
}
