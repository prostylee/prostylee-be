package vn.prostylee.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.voucher.dto.filter.VoucherFilter;
import vn.prostylee.voucher.dto.request.VoucherRequest;
import vn.prostylee.voucher.dto.response.VoucherResponse;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.repository.VoucherRepository;
import vn.prostylee.voucher.service.VoucherService;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final BaseFilterSpecs<Voucher> baseFilterSpecs;
    private final VoucherRepository voucherRepository;

    @Override
    public Page<VoucherResponse> findAll(BaseFilter filter) {
        Specification<Voucher> searchable = buildSearchable((VoucherFilter) filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<Voucher> page = voucherRepository.findAllActive(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, VoucherResponse.class));
    }

    private Specification<Voucher> buildSearchable(VoucherFilter filter) {
        Specification<Voucher> spec = baseFilterSpecs.search(filter);
        if (BooleanUtils.isTrue(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
        } else if (BooleanUtils.isFalse(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
        }

        if (filter.getType() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), filter.getType()));
        }

        if (filter.getStoreId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("storeId"), filter.getStoreId()));
        }

        return spec;
    }

    @Cacheable(cacheNames = CachingKey.VOUCHER, key = "#id")
    @Override
    public VoucherResponse findById(Long id) {
        Voucher voucher = getById(id);
        return BeanUtil.copyProperties(voucher, VoucherResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER, allEntries = true)
    @Override
    public VoucherResponse save(VoucherRequest voucherRequest) {
        Voucher entity = BeanUtil.copyProperties(voucherRequest, Voucher.class);
        Voucher savedEntity = voucherRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, VoucherResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER, allEntries = true)
    @Override
    public VoucherResponse update(Long id, VoucherRequest request) {
        Voucher entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        Voucher savedEntity = voucherRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, VoucherResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            voucherRepository.softDelete(id);
            log.info("Voucher with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Voucher id [{}] does not exists", id);
            return false;
        }
    }

    private Voucher getById(Long id) {
        return voucherRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher is not found with id [" + id + "]"));
    }
}