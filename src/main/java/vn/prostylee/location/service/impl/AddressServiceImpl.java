package vn.prostylee.location.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.entity.Address;
import vn.prostylee.location.repository.AddressRepository;
import vn.prostylee.location.service.AddressService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private static final String PARENT_CODE = "parentCode";

    private final AddressRepository addressRepository;

    private final BaseFilterSpecs<Address> baseFilterSpecs;

    @Override
    public Page<AddressResponse> findAll(BaseFilter baseFilter) {
        AddressFilter addressFilter = (AddressFilter) baseFilter;
        addressFilter.setLimit(ApiParamConstant.UNPAGED_LIMIT_VALUE);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Address> page = addressRepository.findAll(buildAddressSpecification(addressFilter), pageable);
        return page.map(this::toResponse);
    }

    private AddressResponse toResponse(Address address) {
        return BeanUtil.copyProperties(address, AddressResponse.class);
    }

    private Specification<Address> buildAddressSpecification(AddressFilter addressFilter) {
        Specification<Address> spec = baseFilterSpecs.search(addressFilter);
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
            addressRepository.saveAll(addresses);
        }
        return addressDto;
    }

    @Override
    public AddressResponse findByCode(String code) {
        Address address = addressRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("The address is not found with code [" + code + "]"));
        return toResponse(address);
    }

    @Override
    public AddressResponse findByCodeAndParentCode(String code, String parentCode) {
        Address address = addressRepository.findByCodeAndParentCode(code, parentCode)
                .orElseThrow(() -> new ResourceNotFoundException("The address is not found with code [" + code + "]"));
        return toResponse(address);
    }

    @Override
    public boolean isBelongToCodes(String code, List<String> belongToCodes) {
        if (StringUtils.isBlank(code) || CollectionUtils.isNotEmpty(belongToCodes)) {
            return false;
        }

        if (belongToCodes.contains(code)) {
            return true;
        }

        boolean isBelongTo = false;
        final int maxAttempt = 10;
        int attemptTimes = 0;
        while (attemptTimes < maxAttempt) {
            code = getParentCode(code);
            if (StringUtils.isBlank(code) || belongToCodes.contains(code)) {
                isBelongTo = StringUtils.isNotBlank(code);
                break;
            }
            attemptTimes++;
        }

        return isBelongTo;
    }

    @Override
    public boolean isBelongToIds(String code, List<Long> belongToIds) {
        List<String> belongToCodes = addressRepository.findByIds(belongToIds)
                .stream()
                .map(Address::getCode)
                .collect(Collectors.toList());
        return isBelongToCodes(code, belongToCodes);
    }

    private String getParentCode(String code) {
        return addressRepository.findByCode(code)
                .map(Address::getParentCode)
                .orElse(null);
    }
}
