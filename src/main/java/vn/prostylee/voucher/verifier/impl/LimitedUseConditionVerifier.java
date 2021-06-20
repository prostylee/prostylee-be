package vn.prostylee.voucher.verifier.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.voucher.constant.LimitedUseType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.repository.VoucherUserRepository;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

@RequiredArgsConstructor
@Order(60)
@Component
public class LimitedUseConditionVerifier implements VoucherConditionVerifier {

    private final VoucherUserRepository repository;

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        if (voucher.getCndLimitedUseType() == null
                || voucher.getCndLimitedUseType() == LimitedUseType.NONE.getType()
                || voucher.getCndLimitedUseMaxValue() == null
                || voucher.getCndLimitedUseMaxValue() == 0
        ) {
            return true;
        }

        return repository.countVoucher(voucher.getId(), request.getOrder().getBuyerId()) < voucher.getCndLimitedUseMaxValue();
    }
}
