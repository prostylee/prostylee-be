package vn.prostylee.auth.service.impl;

import vn.prostylee.auth.dto.AuthUserDetails;

public interface ExtUserDetailsService {

    /**
     * Get the user based on the user id
     *
     * @param id The id of user
     */
    AuthUserDetails loadUserById(Long id);

    /**
     * Get the user based on the user sub
     *
     * @param sub The sub of user
     */
    AuthUserDetails loadUserBySub(String sub);
}
