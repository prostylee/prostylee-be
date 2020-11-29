package vn.prostylee.core.executor;

import java.util.concurrent.Callable;

public interface CallableService {

    <T> Callable<T> wrap(Callable<T> callable);
}
