package vn.prostylee.auth.role;

import org.springframework.security.access.prepost.PreAuthorize;
import vn.prostylee.auth.constant.AuthRoleConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('" + AuthRoleConstants.STORE_OWNER + "')")
public @interface IsStoreOwner {
}