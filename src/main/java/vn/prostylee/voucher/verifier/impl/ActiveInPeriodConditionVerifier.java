package vn.prostylee.voucher.verifier.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

import java.util.Date;

@Order(0)
@Component
public class ActiveInPeriodConditionVerifier implements VoucherConditionVerifier {

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (BooleanUtils.isNotTrue(voucher.getActive())) {
            return false;
        }

        Date today = new Date();
        return voucher.getCndValidFrom() != null
                && !voucher.getCndValidFrom().after(today)
                && (voucher.getCndValidTo() == null || !voucher.getCndValidTo().before(today));
    }
}
