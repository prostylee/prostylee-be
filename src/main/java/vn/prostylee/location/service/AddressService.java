package vn.prostylee.location.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    Page<AddressResponse> findAll(BaseFilter baseFilter);

    AddressDto imports();

    AddressResponse findByCode(String code);

    AddressResponse findByCodeAndParentCode(String code, String parentCode);

    boolean isBelongToCodes(String code, List<String> belongToCodes);

    boolean isBelongToIds(String code, List<Long> belongToIds);
}
