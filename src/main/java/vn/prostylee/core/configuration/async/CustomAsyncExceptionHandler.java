package vn.prostylee.core.configuration.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Common exception handler for async task
 */
@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		log.error("Ops!", throwable);
		log.error("Exception message - " + throwable.getMessage());
		log.error("Method name - " + method.getName());
		for (Object param : obj) {
			log.error("Parameter value - " + param);
		}
	}

}
