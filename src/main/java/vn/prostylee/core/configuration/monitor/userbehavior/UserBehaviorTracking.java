package vn.prostylee.core.configuration.monitor.userbehavior;

import java.lang.annotation.*;

/**
 * This annotation to mark the endpoint need to tracking user action.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserBehaviorTracking {
}
