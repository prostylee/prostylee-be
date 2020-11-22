package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.response.AccountTempResponse;

public interface AccountTempService {

    AccountTempResponse createAccountTemp(String username);

    boolean isValid(String username, String password);

    boolean delete(String username);
}
