package vn.prostylee.location.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.ApiParamConstant;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.location.dto.AddressDivision;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.filter.AddressFilter;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.entity.Address;
import vn.prostylee.location.repository.AddressRepository;
import vn.prostylee.location.service.AddressService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private static final String PARENT_CODE = "parentCode";

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final AddressRepository addressRepository;

    private final BaseFilterSpecs<Address> baseFilterSpecs;

    @Override
    public Page<AddressResponse> findAll(BaseFilter baseFilter) {
        AddressFilter addressFilter = (AddressFilter) baseFilter;
        addressFilter.setLimit(ApiParamConstant.UNPAGED_LIMIT_VALUE);
        Pageable pageable = this.baseFilterSpecs.page(baseFilter);
        Page<Address> page = this.addressRepository.findAll(buildAddressSpecification(addressFilter), pageable);
        return page.map(this::toResponse);
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

    private AddressResponse toResponse(Address address) {
        return BeanUtil.copyProperties(address, AddressResponse.class);
    }

    private Specification<Address> buildAddressSpecification(AddressFilter addressFilter) {
        Specification<Address> spec = this.baseFilterSpecs.search(addressFilter);
        if (addressFilter.getParentCode() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(PARENT_CODE), addressFilter.getParentCode()));
        } else {
            spec = spec.and((root, query, cb) -> cb.isNull(root.get(PARENT_CODE)));
        }
        return spec;
    }

    @Override
    public AddressDto imports() {
        AddressDto addressDto = JsonUtils.toObject("__files/address.json", AddressDto.class);
        List<AddressDivision> divisions = addressDto.getAdministrativeDivision().getDivisions();
        if (CollectionUtils.isNotEmpty(divisions)) {
            List<Address> addresses = new ArrayList<>();
            divisions.forEach(division -> {
                Address address = Address.builder()
                        .code(division.getCode())
                        .parentCode(division.getParentCode())
                        .name(division.getName())
                        .build();
                addresses.add(address);
            });
        }
        return addressDto;
    }

    @Override
    public AddressResponse findByCode(String code) {
        Address address =  this.addressRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("The address is not found with code [" + code + "]"));
        return this.toResponse(address);
    }

    @Override
    public AddressResponse findByCodeAndParentCode(String code, String parentCode) {
        Address address =  this.addressRepository.findByCodeAndParentCode(code, parentCode)
                .orElseThrow(() -> new ResourceNotFoundException("The address is not found with code [" + code + "]"));
        return this.toResponse(address);
    }
}
