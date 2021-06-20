package vn.prostylee.voucher.repository;

import org.springframework.data.domain.Page;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.dto.response.VoucherUserResponse;

public interface VoucherExtRepository {

    Page<VoucherUserResponse> findAll(VoucherUserFilter filter);
}
