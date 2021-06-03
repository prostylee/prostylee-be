package vn.prostylee.voucher.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.service.MasterDataService;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.voucher.dto.request.VoucherRequest;
import vn.prostylee.voucher.dto.response.VoucherResponse;
import vn.prostylee.voucher.entity.Voucher;

public interface VoucherService extends MasterDataService<VoucherResponse> {

}
