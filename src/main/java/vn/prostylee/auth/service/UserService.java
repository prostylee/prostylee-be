package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.BasicUserResponse;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

import java.util.List;
import java.util.Optional;

public interface UserService extends CrudService<UserRequest, UserResponse, Long>, FieldValueExists, EntityExists<Long> {

    User save(User user);

    boolean isExistsUserByEmail(String email);

    Optional<User> getUserByEmail(String email);

    Optional<User> findBySub(String sub);

    List<UserResponse> findUsersByIds(List<Long> userIds);

    BasicUserResponse getBasicUserInfo(Long userId);
}
