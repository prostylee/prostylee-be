package vn.prostylee.voucher.repository;

import org.springframework.data.domain.Page;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.entity.Voucher;

public interface VoucherExtRepository {

    Page<Voucher> findAll(VoucherUserFilter filter);
}
