package vn.prostylee.auth.service;

import com.google.firebase.auth.FirebaseToken;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;

public interface UserLinkAccountService {
    UserLinkAccount saveBy(FirebaseToken firebaseToken, User user);
}
