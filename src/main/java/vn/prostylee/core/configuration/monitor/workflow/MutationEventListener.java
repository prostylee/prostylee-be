package vn.prostylee.core.configuration.monitor.workflow;

import java.lang.annotation.*;

/**
 * This annotation is used to listen on mutation events and trigger a handler.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MutationEventListener {

    Class<? extends MutationEvent> handler();

    String qualifier() default "";

    MutationAction action() default MutationAction.ALL;

    MutationPhase phase() default MutationPhase.AROUND;
}
