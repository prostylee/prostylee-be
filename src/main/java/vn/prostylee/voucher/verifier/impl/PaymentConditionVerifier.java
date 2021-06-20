package vn.prostylee.voucher.verifier.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.voucher.constant.PaymentType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@Order(10)
@Component
public class PaymentConditionVerifier implements VoucherConditionVerifier {

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndPaymentType() == null || voucher.getCndPaymentType() == PaymentType.ALL.getType()) {
            return true;
        }
        if (request.getOrder().getPaymentTypeId() == null) {
            return false;
        }
        return voucher.getCndPaymentType().longValue() == request.getOrder().getPaymentTypeId();
    }
}
