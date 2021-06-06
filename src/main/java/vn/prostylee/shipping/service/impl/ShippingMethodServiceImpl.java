package vn.prostylee.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import vn.prostylee.shipping.dto.filter.ShippingMethodFilter;
import vn.prostylee.shipping.dto.request.ShippingMethodRequest;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.shipping.entity.ShippingMethod;
import vn.prostylee.shipping.repository.ShippingMethodRepository;
import vn.prostylee.shipping.service.ShippingMethodService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {
    
    private final BaseFilterSpecs<ShippingMethod> baseFilterSpecs;

    private final ShippingMethodRepository shippingMethodRepository;

    @Cacheable(value = CachingKey.SHIPPING_METHOD, key = "#baseFilter")
    @Override
    public Page<ShippingMethodResponse> findAll(BaseFilter baseFilter) {
        ShippingMethodFilter shippingMethodFilter = (ShippingMethodFilter) baseFilter;
        Specification<ShippingMethod> searchable = baseFilterSpecs.search(shippingMethodFilter);
        Pageable pageable = baseFilterSpecs.page(shippingMethodFilter);
        Page<ShippingMethod> page = shippingMethodRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, ShippingMethodResponse.class));
    }

    @Cacheable(cacheNames = CachingKey.SHIPPING_METHOD, key = "#id")
    @Override
    public ShippingMethodResponse findById(Long id) {
        ShippingMethod shippingMethod = getById(id);
        return BeanUtil.copyProperties(shippingMethod, ShippingMethodResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_METHOD, allEntries = true)
    @Override
    public ShippingMethodResponse save(ShippingMethodRequest shippingMethodRequest) {
        ShippingMethod entity = BeanUtil.copyProperties(shippingMethodRequest, ShippingMethod.class);
        ShippingMethod savedEntity = shippingMethodRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, ShippingMethodResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_METHOD, allEntries = true)
    @Override
    public ShippingMethodResponse update(Long id, ShippingMethodRequest request) {
        ShippingMethod entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        ShippingMethod savedEntity = shippingMethodRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, ShippingMethodResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_METHOD, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            shippingMethodRepository.deleteById(id);
            log.info("ShippingMethod with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("ShippingMethod id [{}] does not exists", id);
            return false;
        }
    }

    private ShippingMethod getById(Long id) {
        return shippingMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingMethod is not found with id [" + id + "]"));
    }
}
