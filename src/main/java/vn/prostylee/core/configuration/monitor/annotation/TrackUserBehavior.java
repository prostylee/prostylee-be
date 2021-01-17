package vn.prostylee.core.configuration.monitor.annotation;

import java.lang.annotation.*;

/**
 * This annotation to mark the endpoint need to tracking user action.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackUserBehavior {
}
