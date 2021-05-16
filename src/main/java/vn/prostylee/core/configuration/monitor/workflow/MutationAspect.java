package vn.prostylee.core.configuration.monitor.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Aspect
@Slf4j
public class MutationAspect {

    private final ApplicationContext applicationContext;

    @Pointcut("@annotation(vn.prostylee.core.configuration.monitor.workflow.MutationEventListener)")
    public void annotatedMethod() {}

    @Pointcut("@within(vn.prostylee.core.configuration.monitor.workflow.MutationEventListener)")
    public void annotatedClass() {}

    @Around("execution(* *(..)) && (annotatedMethod() || annotatedClass())")
    public Object handleEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("handleEvent target={}, args={}", joinPoint.getTarget(), joinPoint.getArgs());

        MutationEventListener listener = getMarker(joinPoint);
        if (listener == null) {
            return joinPoint.proceed();
        }

        MutationEvent handler = getHandler(listener);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();

        MutationAction action = listener.action();
        if (listener.action() == MutationAction.ALL) {
            action = MutationAction.findSupportedAction(methodName).orElse(MutationAction.NONE);
        }

        List<Object> args = Arrays.asList(joinPoint.getArgs());
        Object firstArg = null;
        if (CollectionUtils.isNotEmpty(args)) {
            firstArg = args.get(0);
        }

        log.info("Trigger event={}, at phase={}, with args={}", action, listener.phase(), args);

        Object response;
        switch (action) {
            case CREATE:
                if (shouldTriggerPreAction(listener.phase())) {
                    handler.onPreCreate(firstArg);
                }

                response = joinPoint.proceed();

                if (shouldTriggerPostAction(listener.phase())) {
                    handler.onPostCreate(firstArg, response);
                }
                break;

            case UPDATE:
                Object secondArg = null;
                if (args.size() >= 2) {
                    secondArg = args.get(1);
                }
                if (shouldTriggerPreAction(listener.phase())) {
                    handler.onPreUpdate(firstArg, secondArg);
                }

                response = joinPoint.proceed();

                if (shouldTriggerPostAction(listener.phase())) {
                    handler.onPostUpdate(firstArg, secondArg, response);
                }
                break;

            case DELETE:
                if (shouldTriggerPreAction(listener.phase())) {
                    handler.onPreDelete(firstArg);
                }

                response = joinPoint.proceed();

                if (shouldTriggerPostAction(listener.phase())) {
                    handler.onPostDelete(firstArg, response);
                }
                break;

            default:
                response = joinPoint.proceed();
                break;
        }

        return response;
    }

    private MutationEvent getHandler(MutationEventListener listener) {
        Class<? extends MutationEvent> handlerClazz = listener.handler();
        String qualifier = listener.qualifier();
        if (StringUtils.isNotBlank(qualifier)) {
            return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), handlerClazz, qualifier);
        }
        return this.applicationContext.getBean(handlerClazz);
    }

    private MutationEventListener getMarker(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(MutationEventListener.class)) {
            return method.getAnnotation(MutationEventListener.class);
        }

        if (joinPoint.getTarget().getClass().isAnnotationPresent(MutationEventListener.class)) {
            return joinPoint.getTarget().getClass().getAnnotation(MutationEventListener.class);
        }

        return null;
    }

    private boolean shouldTriggerPreAction(MutationPhase phase) {
        return phase == MutationPhase.AROUND || phase == MutationPhase.BEFORE;
    }

    private boolean shouldTriggerPostAction(MutationPhase phase) {
        return phase == MutationPhase.AROUND || phase == MutationPhase.AFTER;
    }
}
