package vn.prostylee.voucher.verifier.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.voucher.constant.BuyAtType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@Order(40)
@Component
public class BuyAtConditionVerifier implements VoucherConditionVerifier {

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndBuyType() == null || voucher.getCndBuyType() == BuyAtType.ALL.getType()) {
            return true;
        }
        if (request.getOrder().getBuyAt() == null) {
            return false;
        }
        return voucher.getCndBuyType().longValue() == request.getOrder().getBuyAt();
    }
}
