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
import vn.prostylee.shipping.dto.request.ShippingProviderRequest;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingProvider;
import vn.prostylee.shipping.repository.ShippingProviderRepository;
import vn.prostylee.shipping.service.ShippingProviderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingProviderServiceImpl implements ShippingProviderService {

    private final BaseFilterSpecs<ShippingProvider> baseFilterSpecs;
    private final ShippingProviderRepository shippingProviderRepository;

    @Cacheable(value = CachingKey.SHIPPING_PROVIDER, key = "#filter")
    @Override
    public Page<ShippingProviderResponse> findAll(BaseFilter filter) {
        Specification<ShippingProvider> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<ShippingProvider> page = shippingProviderRepository.findAll(searchable, pageable);
        return page.map(entity -> {
            ShippingProviderResponse response = BeanUtil.copyProperties(entity, ShippingProviderResponse.class);
            response.setDeliveryTime("Nhận hàng vào 29-12 đến 31-12"); // TODO
            return response;
        });
    }

    @Cacheable(cacheNames = CachingKey.SHIPPING_PROVIDER, key = "#id")
    @Override
    public ShippingProviderResponse findById(Long id) {
        ShippingProvider shippingProvider = getById(id);
        return BeanUtil.copyProperties(shippingProvider, ShippingProviderResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_PROVIDER, allEntries = true)
    @Override
    public ShippingProviderResponse save(ShippingProviderRequest shippingProviderRequest) {
        ShippingProvider entity = BeanUtil.copyProperties(shippingProviderRequest, ShippingProvider.class);
        ShippingProvider savedEntity = shippingProviderRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, ShippingProviderResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_PROVIDER, allEntries = true)
    @Override
    public ShippingProviderResponse update(Long id, ShippingProviderRequest request) {
        ShippingProvider entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        ShippingProvider savedEntity = shippingProviderRepository.save(entity);
        return BeanUtil.copyProperties(savedEntity, ShippingProviderResponse.class);
    }

    @CacheEvict(cacheNames = CachingKey.SHIPPING_PROVIDER, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            shippingProviderRepository.deleteById(id);
            log.info("ShippingProvider with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("ShippingProvider id [{}] does not exists", id);
            return false;
        }
    }

    private ShippingProvider getById(Long id) {
        return shippingProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingProvider is not found with id [" + id + "]"));
    }
}
