package vn.prostylee.auth.service;

import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface SocialAuthService {

    AuthUserDetails login(LoginSocialRequest request);

    boolean canHandle(SocialProviderType type);

    default List<Feature> getFeatures(User user) {
        List<Feature> features = new ArrayList<>();
        user.getRoles().forEach(role -> features.addAll(role.getFeatures()));
        return features;
    }
}
