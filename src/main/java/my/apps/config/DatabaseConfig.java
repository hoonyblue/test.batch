/**
 * copyright 2016 NCIS Cloud Portal System
 * @description
 * <pre></pre>
 *
 * @filename DatabaseConfig.java
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

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
/*
 * Spring 프레임워크의 어노 테이션 기반 트랜잭션 관리를 사용할 수 있도록 한다.
 * <tx:annotation-driven>
 */
//@EnableTransactionManagement
public class DatabaseConfig {

	@Autowired
    ApplicationContext applicationContext;


	/**
	 * DataSource 설정
	 * <bean id="dataSource"
	 * 	class="org.springframework.jdbc.datasource.DriverManagerDataSource">
	 *
	 * 	<property name="driverClassName" value="oracle.jdbc.driver.OracleDriver" />
	 * 	<property name="url" value="jdbc:oracle:thin:@localhost:1521:xe" />
	 * 	<property name="username" value="testuser" />
	 * 	<property name="password" value="pass123!" />
	 * </bean>
	 *
	 * @return
	 */
	@Bean(name="dataSource")
	public DataSource dataSource() {

//	    JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
//	    dsLookup.setResourceRef(true);
//	    DataSource dataSource = dsLookup.getDataSource("java:comp/env/postgresDS");

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.postgresql.Driver");
//		dataSource.setUrl("jdbc:postgresql://10.0.4.59:5432/ncms");
		dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		dataSource.setUrl("jdbc:log4jdbc:postgresql://10.0.4.88:5432/ncms");
		dataSource.setUsername("ncms");
		dataSource.setPassword("ncms1216@");		
		
//		dataSource.setDriverClassName("net.sf.log4jdbc.DriverSpy");
//		dataSource.setUrl("jdbc:log4jdbc:postgresql://10.0.4.59:5432/ncms");		
//		dataSource.setUsername("ncms");
//		dataSource.setPassword("ncms1216@");
		
		return dataSource;
	}

	/**
	 * TransactionManager설정
	 *
	 * <bean id="transactionManager"
	 * 	class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
	 * 	<property name="dataSource" ref="dataSource"></property>
	 * </bean>
	 *
	 * @return
	 */
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	/**
	 * SqlSessionFactory 설정
	 *
	 * <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	 * 	<property name="dataSource" ref="dataSource" />
	 * 	<property name="configLocation" value="classpath:mybatis/configuration.xml" />
	 *  <property name="mapperLocations" value="classpath:mybatis/mappers/** /*.xml" />
	 * </bean>
	 *
	 * @param dataSource
	 * @param applicationContext
	 * @return
	 * @throws IOException
	 */
	@Bean(name="sqlSessionFactoryBean")
	public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource,
			ApplicationContext applicationContext) throws IOException {

		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/configuration.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mappers/**/*.xml"));

		return factoryBean;
	}

	/**
	 * SqlSessionTemplate 설정
	 *
	 * <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
	 * 	<constructor-arg ref="sqlSessionFactory" />
	 * </bean>
	 *
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean(name="sqlSession", destroyMethod="clearCache")
	 public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryBean")SqlSessionFactory sqlSessionFactory) {
	     return new SqlSessionTemplate(sqlSessionFactory);
	 }

}
