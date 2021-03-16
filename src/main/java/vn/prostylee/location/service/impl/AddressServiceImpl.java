package vn.prostylee.location.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.location.dto.AddressDivision;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.entity.Address;
import vn.prostylee.location.repository.AddressRepository;
import vn.prostylee.location.service.AddressService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

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

    @Override
    public AddressDto imports() {
        AddressDto addressDto = parse("__files/address.json", AddressDto.class);
        List<AddressDivision> divisions = addressDto.getAdministrativeDivision().getDivisions();
        if (CollectionUtils.isNotEmpty(divisions)) {
            List<Address> addresses = new ArrayList<>();
            for (AddressDivision division : divisions) {
                Address address = Address.builder().code(division.getCode()).parentCode(division.getParentCode()).name(division.getName()).build();
                addresses.add(address);
            }
            if (!addresses.isEmpty()) {
                this.addressRepository.saveAll(addresses);
            }
        }
        return addressDto;
    }

    private static InputStream getResourceAsStream(String file) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(file);
    }

    public static <T> T parse(String file, Class<T> clazz) {
        try {
            return objectMapper.readValue(getResourceAsStream(file), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
