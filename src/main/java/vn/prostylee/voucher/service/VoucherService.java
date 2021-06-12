package vn.prostylee.voucher.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.voucher.dto.request.VoucherRequest;
import vn.prostylee.voucher.dto.response.VoucherResponse;

public interface VoucherService extends CrudService<VoucherRequest, VoucherResponse, Long> {

}
