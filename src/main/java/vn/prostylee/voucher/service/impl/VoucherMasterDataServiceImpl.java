package vn.prostylee.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.voucher.dto.filter.VoucherMasterDataFilter;
import vn.prostylee.voucher.dto.request.VoucherMasterDataRequest;
import vn.prostylee.voucher.dto.response.VoucherMasterDataResponse;
import vn.prostylee.voucher.entity.VoucherMasterData;
import vn.prostylee.voucher.repository.VoucherMasterDataRepository;
import vn.prostylee.voucher.service.VoucherMasterDataService;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherMasterDataServiceImpl implements VoucherMasterDataService {

    private final BaseFilterSpecs<VoucherMasterData> baseFilterSpecs;

    private final VoucherMasterDataRepository voucherMasterDataRepository;

    @Cacheable(value = CachingKey.VOUCHER_MASTER_DATA, key = "#baseFilter")
    @Override
    public Page<VoucherMasterDataResponse> findAll(BaseFilter baseFilter) {
        VoucherMasterDataFilter voucherMasterDataFilter = (VoucherMasterDataFilter) baseFilter;
        Specification<VoucherMasterData> searchable = buildSearchable(voucherMasterDataFilter);
        Pageable pageable = baseFilterSpecs.page(voucherMasterDataFilter);
        Page<VoucherMasterData> page = voucherMasterDataRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, VoucherMasterDataResponse.class));
    }

    private Specification<VoucherMasterData> buildSearchable(VoucherMasterDataFilter filter) {
        Specification<VoucherMasterData> spec = baseFilterSpecs.search(filter);
        if (BooleanUtils.isTrue(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
        } else if (BooleanUtils.isFalse(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
        }

        if (filter.getGroup() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("group"), filter.getGroup()));
        }

        return spec;
    }

    @Cacheable(cacheNames = CachingKey.VOUCHER_MASTER_DATA, key = "#id")
    @Override
    public VoucherMasterDataResponse findById(Long id) {
        VoucherMasterData voucherMasterData = getById(id);
        return BeanUtil.copyProperties(voucherMasterData, VoucherMasterDataResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER_MASTER_DATA, allEntries = true)
    @Override
    public VoucherMasterDataResponse save(VoucherMasterDataRequest voucherMasterDataRequest) {
        VoucherMasterData entity = BeanUtil.copyProperties(voucherMasterDataRequest, VoucherMasterData.class);
        VoucherMasterData savedEntity = voucherMasterDataRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, VoucherMasterDataResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER_MASTER_DATA, allEntries = true)
    @Override
    public VoucherMasterDataResponse update(Long id, VoucherMasterDataRequest request) {
        VoucherMasterData entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        VoucherMasterData savedEntity = voucherMasterDataRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, VoucherMasterDataResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.VOUCHER_MASTER_DATA, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Unsupported deletion action on VoucherMasterData, please inactive it instead of deleting");
    }

    private VoucherMasterData getById(Long id) {
        return voucherMasterDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VoucherMasterData is not found with id [" + id + "]"));
    }
}