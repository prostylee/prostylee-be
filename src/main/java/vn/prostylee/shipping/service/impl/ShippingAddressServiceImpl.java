package vn.prostylee.shipping.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.entity.ShippingAddress;
import vn.prostylee.shipping.repository.ShippingAddressRepository;
import vn.prostylee.shipping.service.ShippingAddressService;

@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;

    @Cacheable(value = CachingKey.SHIPPING_ADDRESS, key = "#filter")
    @Override
    public Page<ShippingAddressResponse> findAll(MasterDataFilter filter) {
        throw new UnsupportedOperationException();
    }

    @Cacheable(value = CachingKey.SHIPPING_ADDRESS, key = "#id")
    @Override
    public ShippingAddress getShippingAddressById(Long id) {
        return shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address is not found with id [" + id + "]"));
    }
}
