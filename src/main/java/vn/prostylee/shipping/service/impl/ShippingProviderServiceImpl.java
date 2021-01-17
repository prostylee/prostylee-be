package vn.prostylee.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingProvider;
import vn.prostylee.shipping.repository.ShippingProviderRepository;
import vn.prostylee.shipping.service.ShippingProviderService;

@Service
@RequiredArgsConstructor
public class ShippingProviderServiceImpl implements ShippingProviderService {
    private final BaseFilterSpecs<ShippingProvider> baseFilterSpecs;

    private final ShippingProviderRepository shippingProviderRepository;
    @Override
    public Page<ShippingProviderResponse> findAll(MasterDataFilter filter) {
        Specification<ShippingProvider> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<ShippingProvider> page = shippingProviderRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, ShippingProviderResponse.class));
    }
}