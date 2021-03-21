package vn.prostylee.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.filter.UserAddressFilter;
import vn.prostylee.auth.dto.request.UserAddressRequest;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserAddress;
import vn.prostylee.auth.repository.UserAddressRepository;
import vn.prostylee.auth.service.UserAddressService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.exception.ValidatingException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.service.AddressService;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserAddressServiceImpl implements UserAddressService {

    private final AuthenticatedProvider authenticatedProvider;

    private final UserAddressRepository userAddressRepository;

    private final BaseFilterSpecs<UserAddress> baseFilterSpecs;

    private final AddressService addressService;

    public UserAddressServiceImpl(AuthenticatedProvider authenticatedProvider,
                                  UserAddressRepository userAddressRepository,
                                  BaseFilterSpecs<UserAddress> baseFilterSpecs, AddressService addressService) {
        this.authenticatedProvider = authenticatedProvider;
        this.userAddressRepository = userAddressRepository;
        this.baseFilterSpecs = baseFilterSpecs;
        this.addressService = addressService;
    }

    @Override
    public Page<UserAddressResponse> findAll(BaseFilter baseFilter) {
        UserAddressFilter userAddressFilter = (UserAddressFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(userAddressFilter);
        Page<UserAddress> page = this.userAddressRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    @Override
    public UserAddressResponse findById(Long id) {
        return this.toResponse(this.getById(id));
    }

    @Override
    public UserAddressResponse save(UserAddressRequest request) {
        UserAddress userAddress = BeanUtil.copyProperties(request, UserAddress.class);
        return this.buildUserAddress(userAddress, request);
    }

    @Override
    public UserAddressResponse update(Long id, UserAddressRequest request) {
        UserAddress userAddress = this.getById(id);
        return this.buildUserAddress(userAddress, request);
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<UserAddress> userAddress = this.userAddressRepository.findById(id);
        if (userAddress.isPresent() && userAddress.get().getPriority())
            throw new ValidatingException("You cannot delete primary address with id [" + id + "]");
        try {
            this.userAddressRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Address id {} does not exists", id);
            return false;
        }
    }

    @Override
    public boolean isEntityExists(Long aLong, Map<String, Object> uniqueValues) {
        return false;
    }

    @Override
    public boolean isFieldValueExists(String fieldName, Object value) {
        return false;
    }

    @Override
    public void savePriority(Boolean priority) {
        if (!priority) {
            return;
        }
        UserAddress userAddress =  this.userAddressRepository.findOneByUserIdAndPriority(this.getLoggedUserId(), true);
        if (userAddress != null) {
            userAddress.setPriority(false);
            this.userAddressRepository.save(userAddress);
        }
    }

    private UserAddressResponse buildUserAddress(UserAddress userAddress, UserAddressRequest request) {
        AddressResponse city = this.addressService.findByCode(request.getCityCode());
        AddressResponse district = this.addressService.findByCodeAndParentCode(request.getDistrictCode(), city.getCode());
        AddressResponse ward = this.addressService.findByCodeAndParentCode(request.getWardCode(), district.getCode());
        userAddress.setUser(User.builder().id(this.getLoggedUserId()).build());
        String fullAddress = String.format("%s,%s,%s,%s", request.getAddress(), ward.getName(), district.getName(), city.getName());
        userAddress.setCityCode(request.getCityCode());
        userAddress.setDistrictCode(request.getDistrictCode());
        userAddress.setWardCode(request.getWardCode());
        userAddress.setAddress(request.getAddress());
        userAddress.setFullAddress(fullAddress);
        userAddress.setPriority(request.getPriority());
        this.savePriority(request.getPriority());
        return this.toResponse(this.userAddressRepository.save(userAddress));
    }

    private UserAddress getById(Long id) {
        return this.userAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address is not found with id [" + id + "]"));
    }

    private Long getLoggedUserId() {
        return this.authenticatedProvider.getUserIdValue();
    }

    private UserAddressResponse toResponse(UserAddress userAddress) {
        return BeanUtil.copyProperties(userAddress, UserAddressResponse.class);
    }
}
