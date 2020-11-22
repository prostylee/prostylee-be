package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.AccountRequest;
import vn.prostylee.auth.dto.response.AccountResponse;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

public interface AccountService extends CrudService<AccountRequest, AccountResponse, Long>, FieldValueExists, EntityExists<Long> {

    AccountResponse findByPushToken(String pushToken);
}
