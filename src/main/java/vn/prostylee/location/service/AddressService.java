package vn.prostylee.location.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.response.AddressResponse;

public interface AddressService {

    Page<AddressResponse> findAll(BaseFilter baseFilter);

    AddressDto imports();

    AddressResponse findByCode(String code);

    AddressResponse findByCodeAndParentCode(String code, String parentCode);
}
