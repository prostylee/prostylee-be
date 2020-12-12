package vn.prostylee.core.configuration.async;

import vn.prostylee.core.configuration.properties.ThreadExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Config asynchronous execution support in Spring
 *
 * The @Async has two limitations: <br />
 *
 * (1) it must be applied to public methods only <br />
 * (2) self-invocation – calling the async method from within the same class – won't work <br />
 * The reasons are simple – the method needs to be public so that it can be proxied. <br />
 * And self-invocation doesn't work because it bypasses the proxy and calls the underlying method directly.
 */
@RequiredArgsConstructor
@Configuration
@EnableAsync
public class SpringAsyncConfig extends AsyncConfigurerSupport {

    private static final String THREAD_NAME_PREFIX = "ProStylee-Async-";
    private final ThreadExecutor threadExecutor;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadExecutor.getCorePoolSize());
        executor.setMaxPoolSize(threadExecutor.getMaxPoolSize());
        executor.setQueueCapacity(threadExecutor.getQueueCapacity());
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
