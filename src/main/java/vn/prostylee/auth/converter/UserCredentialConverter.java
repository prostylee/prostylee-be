package vn.prostylee.auth.converter;

import org.springframework.stereotype.Component;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.core.utils.BeanUtil;

@Component
public class UserCredentialConverter {

    public UserCredential convert(AuthUserDetails userDetail) {
        UserCredential user = BeanUtil.copyProperties(userDetail, UserCredential.class);
        user.setRoles(new RoleConverter().convert(userDetail.getRoles()));
        user.setFeatures(userDetail.getFeatures());
        return user;
    }
}
