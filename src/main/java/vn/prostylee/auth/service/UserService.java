package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

import java.util.Optional;

public interface UserService extends CrudService<UserRequest, UserResponse, Long>, FieldValueExists, EntityExists<Long> {

    UserResponse findByPushToken(String pushToken);

    User save(User user);

    boolean isExistsUserByEmail(String email);

    Optional<User> getUserByEmail(String email);
}
