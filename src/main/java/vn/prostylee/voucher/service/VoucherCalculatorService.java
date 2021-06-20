package vn.prostylee.voucher.service;

import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.entity.Voucher;

public interface VoucherCalculatorService {

    VoucherCalculationResponse calculateDiscount(Voucher voucher, VoucherCalculationRequest request);
}
