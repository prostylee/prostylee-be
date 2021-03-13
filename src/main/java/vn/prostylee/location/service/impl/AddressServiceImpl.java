package vn.prostylee.location.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.service.AddressService;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {
    @Override
    public Page<AddressResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public AddressResponse findById(Long aLong) {
        return null;
    }

    @Override
    public AddressResponse save(AddressRequest addressRequest) {
        return null;
    }

    @Override
    public AddressResponse update(Long aLong, AddressRequest s) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
