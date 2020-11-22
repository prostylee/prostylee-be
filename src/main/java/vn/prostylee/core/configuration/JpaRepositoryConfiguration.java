package vn.prostylee.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.prostylee.ProStyleeApplication;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = ProStyleeApplication.class, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class JpaRepositoryConfiguration {

}
