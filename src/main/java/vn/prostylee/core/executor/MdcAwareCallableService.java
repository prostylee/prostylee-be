package vn.prostylee.core.executor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class MdcAwareCallableService<T> implements Callable<T> {

    private final Map<String, String> contextMap = MDC.getCopyOfContextMap();
    private Callable<T> callable;

    public MdcAwareCallableService(Callable<T> callable) {
        this.callable = callable;
        log.trace("Init MdcAwareCallableService currentThreadName={}, contextMap={}", Thread.currentThread().getName(), contextMap);
    }

    @Override
    public T call() throws Exception {
        if (contextMap != null) {
            log.trace("Call MdcAwareCallableService currentThreadName={}, contextMap={}", Thread.currentThread().getName(), contextMap);
            MDC.setContextMap(contextMap);
        }
        try {
            return callable.call();
        } finally {
            MDC.clear();
        }
    }
}
