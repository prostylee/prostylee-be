package vn.prostylee.voucher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.voucher.dto.filter.VoucherMasterDataFilter;
import vn.prostylee.voucher.dto.request.VoucherMasterDataRequest;
import vn.prostylee.voucher.dto.response.VoucherMasterDataResponse;
import vn.prostylee.voucher.service.VoucherMasterDataService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/masters/vouchers")
public class VoucherMasterDataController extends CrudController<VoucherMasterDataRequest, VoucherMasterDataResponse, Long, VoucherMasterDataFilter> {

    public VoucherMasterDataController(VoucherMasterDataService service) {
        super(service);
    }
}
