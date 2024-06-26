package vn.prostylee.core.configuration.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

@Order(0)
@Slf4j
@RequiredArgsConstructor
@Configuration
@Aspect
public class TransactionAdviceInterceptor {
	private static final String TX_METHOD_NAME = "*"; // All method (*)

	private static final Integer TX_METHOD_TIMEOUT = -1; // Never timeout (-1)

	private static final String AOP_POINTCUT_EXPRESSION = "execution(public * vn.prostylee..*.controller..*.*(..))";

	private final TransactionManager transactionManager;

	@Bean
	public TransactionInterceptor txAdvice() {
		log.info("Init bean TransactionAdviceInterceptor > TransactionInterceptor");
		MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();

		// set Interceptor attribute
		transactionAttribute.setName(TX_METHOD_NAME);

		// define Rollback rules event (when to trigger)
		transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		transactionAttribute.setTimeout(TX_METHOD_TIMEOUT);
//		transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);

		// set Interceptor source
		source.setTransactionAttribute(transactionAttribute);
		return new TransactionInterceptor(transactionManager, source);
	}

	@Bean
	public Advisor txAdviceAdvisor() {
		log.info("Init bean TransactionAdviceInterceptor > Advisor");
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION); // define Pointcut (where - looking for trigger)
		return new DefaultPointcutAdvisor(pointcut, txAdvice());
	}
}
