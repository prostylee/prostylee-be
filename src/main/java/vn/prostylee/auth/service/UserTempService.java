package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.response.UserTempResponse;

public interface UserTempService {

    UserTempResponse createUserTemp(String username);

    boolean isValid(String username, String password);

    boolean delete(String username);
}
