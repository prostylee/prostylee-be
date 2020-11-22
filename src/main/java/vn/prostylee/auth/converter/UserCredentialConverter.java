package vn.prostylee.auth.converter;

import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.core.utils.BeanUtil;

public class UserCredentialConverter {

    public UserCredential convert(AuthUserDetails userDetail) {
        UserCredential user = BeanUtil.copyProperties(userDetail, UserCredential.class);
        user.setRoles(new RoleConverter().convert(userDetail.getRoles()));
        user.setFeatures(userDetail.getFeatures());
        return user;
    }
}
