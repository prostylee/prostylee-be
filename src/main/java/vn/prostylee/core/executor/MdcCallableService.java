package vn.prostylee.core.executor;

import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

@Service
public class MdcCallableService implements CallableService {

    @Override
    public <T> Callable<T> wrap(Callable<T> callable) {
        return new MdcAwareCallableService<>(callable);
    }
}
