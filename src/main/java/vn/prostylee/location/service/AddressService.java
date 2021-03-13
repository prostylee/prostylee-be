package vn.prostylee.location.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;

public interface AddressService extends CrudService<AddressRequest, AddressResponse, Long> {
}
