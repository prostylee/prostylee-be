package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.filter.BrandFilter;
import vn.prostylee.product.dto.request.BrandRequest;
import vn.prostylee.product.dto.response.BrandResponse;
import vn.prostylee.product.entity.Brand;
import vn.prostylee.product.repository.BrandRepository;
import vn.prostylee.product.service.BrandService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BaseFilterSpecs<Brand> baseFilterSpecs;

    @Override
    public Page<BrandResponse> findAll(BaseFilter baseFilter) {
        BrandFilter brandFilter = (BrandFilter) baseFilter;
        Specification<Brand> searchable = baseFilterSpecs.search(brandFilter);
        Pageable pageable = baseFilterSpecs.page(brandFilter);
        Page<Brand> page = brandRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, BrandResponse.class));
    }

    @Override
    public BrandResponse findById(Long id) {
        Brand brand = getById(id);
        return BeanUtil.copyProperties(brand, BrandResponse.class);
    }

    @Override
    public BrandResponse save(BrandRequest brandRequest) {
        Brand entity = BeanUtil.copyProperties(brandRequest, Brand.class);
        Brand savedEntity = brandRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, BrandResponse.class);
    }

    @Override
    public BrandResponse update(Long id, BrandRequest request) {
        Brand entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        Brand savedEntity = brandRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, BrandResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            brandRepository.deleteById(id);
            log.info("Brand with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Brand id [{}] does not exists", id);
            return false;
        }
    }

    private Brand getById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand is not found with id [" + id + "]"));
    }
}
