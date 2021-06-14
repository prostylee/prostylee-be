package vn.prostylee.voucher.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherUserRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.dto.response.VoucherUserResponse;

public interface VoucherUserService extends CrudService<VoucherUserRequest, VoucherUserResponse, Long> {

    VoucherUserResponse saveByVoucherId(Long voucherId);

    boolean deleteByVoucherId(Long voucherId);

    VoucherCalculationResponse calculateDiscount(Long voucherId, VoucherCalculationRequest request);

    boolean verifyVoucher(Long voucherId, VoucherCalculationRequest request);
}
