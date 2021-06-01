package vn.prostylee.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.shipping.dto.request.ShippingAddressRequest;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingProvider;
import vn.prostylee.shipping.repository.ShippingProviderRepository;
import vn.prostylee.shipping.service.ShippingProviderService;

@Service
@RequiredArgsConstructor
public class ShippingProviderServiceImpl implements ShippingProviderService {

    private final BaseFilterSpecs<ShippingProvider> baseFilterSpecs;
    private final ShippingProviderRepository shippingProviderRepository;

    @Cacheable(value = CachingKey.SHIPPING_PROVIDER, key = "#filter")
    @Override
    public Page<ShippingProviderResponse> findAll(MasterDataFilter filter) {
        Specification<ShippingProvider> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<ShippingProvider> page = shippingProviderRepository.findAll(searchable, pageable);
        return page.map(entity -> {
            ShippingProviderResponse response = BeanUtil.copyProperties(entity, ShippingProviderResponse.class);
            response.setDeliveryTime("Nhận hàng vào 29-12 đến 31-12");
            return response;
        });
    }

    @Cacheable(value = CachingKey.SHIPPING_PROVIDER, key = "#id")
    @Override
    public ShippingProvider getShippingProviderById(Long id) {
        return shippingProviderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping provider is not found with id [" + id + "]"));
    }
}
