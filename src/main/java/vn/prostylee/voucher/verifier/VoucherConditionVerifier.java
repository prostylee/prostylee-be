package vn.prostylee.voucher.verifier;

import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.entity.Voucher;

public interface VoucherConditionVerifier {

    boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request);
}
