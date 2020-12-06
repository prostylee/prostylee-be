package vn.prostylee.auth.service;

import com.google.firebase.auth.FirebaseToken;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.dto.response.ZaloResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;

public interface UserService extends CrudService<UserRequest, UserResponse, Long>, FieldValueExists, EntityExists<Long> {

    UserResponse findByPushToken(String pushToken);

    User save(FirebaseToken firebaseToken);

    User save(ZaloResponse zaloResponse);
}
