package vn.prostylee.auth.service;

import org.springframework.data.domain.Page;
import vn.prostylee.auth.dto.request.UserAddressRequest;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

public interface UserAddressService extends CrudService<UserAddressRequest, UserAddressResponse, Long>, FieldValueExists, EntityExists<Long> {

    Page<UserAddressResponse> findByUserId(Long userId);
}
