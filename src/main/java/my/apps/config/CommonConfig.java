/**
 * copyright 2016 NCIS Cloud Portal System
 * @description
 * <pre></pre>
 *
 * @filename CommonConfig.java
 *
 * @author 최진호
 * @lastmodifier 최진호
 * @created 2016. 10. 4.
 * @lastmodified 2016. 10. 4.
 *
 * @history
 * -----------------------------------------------------------
 * Date         author         ver            Description
 * -----------------------------------------------------------
 * 2016. 10. 4.     최진호         v1.0             최초생성
 *
 */
package my.apps.config;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
/*
 * <context:component-scan base-package="com.company">
 *		<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
 *	</context:component-scan>
 */
@ComponentScan(
		basePackages="my",
		excludeFilters={
				@ComponentScan.Filter(Controller.class)
		}
	)
public class CommonConfig {

	/**
	 * 해당 설정이 없을 경우 @Autowired 를 찾지 못하는 경우가 생깁니다.
	 * 하여 Validator을 Bean으로 설정을 합니다.
	 * @return
	 */
	@Bean
	public Validator localValidatorFactoroyBean() {
		return new LocalValidatorFactoryBean();
	}

}
