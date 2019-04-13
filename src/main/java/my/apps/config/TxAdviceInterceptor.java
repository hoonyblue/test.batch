/**
 * copyright 2016 NCIS Cloud Portal System
 * @description
 * <pre></pre>
 *
 * @filename TxAdviceInterceptor.java
 * 
 * @author gnsl
 * @lastmodifier gnsl
 * @created 2017. 10. 20.
 * @lastmodified 2017. 10. 20.
 *
 * @history
 * -----------------------------------------------------------
 * Date         author         ver            Description
 * -----------------------------------------------------------
 * 2017. 10. 20.     gnsl         v1.0             최초생성
 *
 */
package my.apps.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

/**
 * @author gnsl
 *
 */
@Configuration
public class TxAdviceInterceptor {

	private static final String TX_METHOD_NAME = "*";
    private static final int TX_METHOD_TIMEOUT = 3;
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* ncis..service.impl.*Impl.*(..)) and !execution(* ncis..service.impl.*Impl.select*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
        transactionAttribute.setName(TX_METHOD_NAME);
        transactionAttribute.setRollbackRules(
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        transactionAttribute.setTimeout(TX_METHOD_TIMEOUT);
        source.setTransactionAttribute(transactionAttribute);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
    
}
