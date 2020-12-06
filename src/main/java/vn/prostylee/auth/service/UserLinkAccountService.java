package vn.prostylee.auth.service;

import com.google.firebase.auth.FirebaseToken;
import vn.prostylee.auth.entity.UserLinkAccount;

import java.util.Optional;

public interface UserLinkAccountService {

    Optional<UserLinkAccount> getUserLinkAccountBy(String Id);
}
