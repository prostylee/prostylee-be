package vn.prostylee.core.configuration;

import org.springframework.data.domain.AuditorAware;
import vn.prostylee.core.provider.AuthenticatedProvider;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    private final AuthenticatedProvider authenticatedProvider;

    public AuditorAwareImpl(AuthenticatedProvider authenticatedProvider) {
        this.authenticatedProvider = authenticatedProvider;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        return authenticatedProvider.getUserId();
    }
}