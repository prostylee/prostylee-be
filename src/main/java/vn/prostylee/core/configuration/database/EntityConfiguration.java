package vn.prostylee.core.configuration.database;

import vn.prostylee.core.provider.AuthenticatedProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import vn.prostylee.ProStyleeApplication;

@Configuration
@EntityScan(basePackageClasses = { ProStyleeApplication.class, Jsr310JpaConverters.class })
@EnableJpaAuditing
public class EntityConfiguration {

    private final AuthenticatedProvider authenticatedProvider;

    @Autowired
    public EntityConfiguration(AuthenticatedProvider authenticatedProvider) {
        this.authenticatedProvider = authenticatedProvider;
    }

    @Bean
    public AuditorAware<Long> auditorAware(){
        return new AuditorAwareImpl(authenticatedProvider);
    }

}
