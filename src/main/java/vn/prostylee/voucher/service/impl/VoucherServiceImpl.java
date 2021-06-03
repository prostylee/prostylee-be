package vn.prostylee.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.entity.Category;
import vn.prostylee.shipping.entity.ShippingMethod;
import vn.prostylee.voucher.dto.filter.VoucherFilter;
import vn.prostylee.voucher.dto.request.VoucherRequest;
import vn.prostylee.voucher.dto.response.VoucherResponse;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.repository.VoucherRepository;
import vn.prostylee.voucher.service.VoucherService;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final BaseFilterSpecs<Voucher> baseFilterSpecs;

    private final VoucherRepository voucherRepository;

    @Override
    public Page<VoucherResponse> findAll(MasterDataFilter filter) {
        Specification<Voucher> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<Voucher> page = this.voucherRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    private VoucherResponse toResponse(Voucher voucher) {
        return BeanUtil.copyProperties(voucher, VoucherResponse.class);
    }
}
