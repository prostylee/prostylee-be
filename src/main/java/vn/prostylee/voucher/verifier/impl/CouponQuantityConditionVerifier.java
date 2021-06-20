package vn.prostylee.voucher.verifier.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.voucher.constant.CouponQuantityType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.repository.VoucherUserRepository;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@RequiredArgsConstructor
@Order(70)
@Component
public class CouponQuantityConditionVerifier implements VoucherConditionVerifier {

    private final VoucherUserRepository repository;

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndCouponQuantityType() == null
                || voucher.getCndCouponQuantityType() == CouponQuantityType.NONE.getType()
                || voucher.getCndCouponQuantityMaxValue() == null
                || voucher.getCndCouponQuantityMaxValue() == 0
        ) {
            return true;
        }

        return repository.countVoucher(voucher.getId(), null) < voucher.getCndCouponQuantityMaxValue();
    }
}
