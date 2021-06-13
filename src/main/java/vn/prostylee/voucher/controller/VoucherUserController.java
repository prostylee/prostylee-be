package vn.prostylee.voucher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.dto.response.VoucherUserResponse;
import vn.prostylee.voucher.service.VoucherUserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/user-vouchers")
public class VoucherUserController {

    private final VoucherUserService service;

    @GetMapping
    public Page<VoucherUserResponse> getVouchers(@Valid VoucherUserFilter filter) {
        return service.findAll(filter);
    }

    @PostMapping("/{voucherId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VoucherUserResponse save(@PathVariable Long voucherId) {
        return service.saveByVoucherId(voucherId);
    }

    @DeleteMapping("/{voucherId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public SimpleResponse delete(@PathVariable Long voucherId) {
        return SimpleResponse.builder().data(service.deleteByVoucherId(voucherId)).build();
    }

    @PostMapping("/{voucherId}/calculate")
    public VoucherCalculationResponse calculateDiscount(@PathVariable Long voucherId, @Valid VoucherCalculationRequest request) {
        return service.calculateDiscount(voucherId, request);
    }

    @PostMapping("/{voucherId}/verify")
    public SimpleResponse verifyVoucher(@PathVariable Long voucherId, @Valid VoucherCalculationRequest request) {
        return SimpleResponse.builder().data(service.verifyVoucher(voucherId, request)).build();
    }

}
