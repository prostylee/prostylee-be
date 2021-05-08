package vn.prostylee.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.shipping.entity.ShippingMethod;
import vn.prostylee.shipping.repository.ShippingMethodRepository;
import vn.prostylee.shipping.service.ShippingMethodService;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {
    private final BaseFilterSpecs<ShippingMethod> baseFilterSpecs;

    private final ShippingMethodRepository shippingMethodRepository;

    @Cacheable(value = CachingKey.SHIPPING_METHOD, key = "#filter")
    @Override
    public Page<ShippingMethodResponse> findAll(MasterDataFilter filter) {
        Specification<ShippingMethod> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<ShippingMethod> page = shippingMethodRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, ShippingMethodResponse.class));
    }
}
