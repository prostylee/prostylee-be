package vn.prostylee.voucher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.voucher.dto.filter.VoucherFilter;
import vn.prostylee.voucher.dto.request.VoucherRequest;
import vn.prostylee.voucher.dto.response.VoucherResponse;
import vn.prostylee.voucher.service.VoucherService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/vouchers")
public class VoucherController extends CrudController<VoucherRequest, VoucherResponse, Long, VoucherFilter> {

    public VoucherController(VoucherService service) {
        super(service);
    }
}
