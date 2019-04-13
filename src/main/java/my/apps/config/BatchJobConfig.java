package my.apps.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;

import egovframework.rte.bat.core.launch.support.EgovBatchRunner;

@Configuration
public class BatchJobConfig {

	/**
     * <bean id="eGovBatchRunner" class="egovframework.rte.bat.core.launch.support.EgovBatchRunner">
     *   <constructor-arg ref="jobOperator" />
     *   <constructor-arg ref="jobExplorer" />
     *   <constructor-arg ref="jobRepository" />
     * </bean>
	 */
	@Bean(name="egovBatchRunner")
	public EgovBatchRunner egovBatchRunner(JobOperator jobOperator, JobExplorer jobExplorer, 
			JobRepository jobRepository) {
		return new EgovBatchRunner(jobOperator, jobExplorer, jobRepository);
	}
	
	/**
//<bean id="jobLauncher" class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
//	<property name="jobRepository" ref="jobRepository" />
//</bean>
     */
	@Bean(name="jobLauncher")
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}
	
	/**
	 * <bean class="org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor">
	 *    <property name="jobRegistry" ref="jobRegistry" />
	 * </bean>
     */
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		return jobRegistryBeanPostProcessor;
	}
	
	/**
	 * <!-- cubrid usage - comment -->
	 * <bean id="jobRepository" class="org.springframework.batch.core.repository.support.JobRepositoryFactoryBean"
	 * 	 p:dataSource-ref="dataSource" p:transactionManager-ref="transactionManager"
	 * 	 p:lobHandler-ref="lobHandler" />
	 */
	@Bean(name="jobRepository")
	public JobRepositoryFactoryBean jobRepository(DataSource dataSource, 
			PlatformTransactionManager transactionManager,
			LobHandler lobHandler) {
		JobRepositoryFactoryBean jobRepository = new JobRepositoryFactoryBean();
		jobRepository.setDataSource(dataSource);
		jobRepository.setTransactionManager(transactionManager);
		jobRepository.setLobHandler(lobHandler);
		return jobRepository;
	}
	
	/**
	 * <bean id="jobOperator" class="org.springframework.batch.core.launch.support.SimpleJobOperator"
	 *   p:jobLauncher-ref="jobLauncher" p:jobExplorer-ref="jobExplorer"
	 *   p:jobRepository-ref="jobRepository" p:jobRegistry-ref="jobRegistry" />
	 */
	@Bean
	public JobOperator jobOperator(JobLauncher jobLauncher, JobExplorer jobExplorer,
			JobRepository jobRepository, JobRegistry jobRegistry) {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobLauncher(jobLauncher);
		jobOperator.setJobExplorer(jobExplorer); 
		jobOperator.setJobRepository(jobRepository);
		jobOperator.setJobRegistry(jobRegistry);
		
		return jobOperator;
	}

	/**
	 * <bean id="jobExplorer" class="org.springframework.batch.core.explore.support.JobExplorerFactoryBean"
	 *	 p:dataSource-ref="dataSource" />
	 */
	@Bean(name="jobExplorer")
	public JobExplorerFactoryBean jobExplorer(DataSource dataSource) {
		JobExplorerFactoryBean jobExplorer = new JobExplorerFactoryBean();
		jobExplorer.setDataSource(dataSource);
		return jobExplorer;
	}
	
	/**
	 * <bean id="jobRegistry" class="org.springframework.batch.core.configuration.support.MapJobRegistry" />
	 */
	@Bean(name="jobRegistry")
	public MapJobRegistry jobRegistry() {
		return new MapJobRegistry();
	}

	/**
	 * <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
	 * 	 <property name="dataSource" ref="dataSource" />
	 * </bean>
	 */
	@Bean(name="jdbcTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
	
	@Bean(name="jobBuilderFactory")
	public JobBuilderFactory jobBuilderFactory(@Qualifier("jobRepository")JobRepository jobRepository) {
		JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(jobRepository);
		return jobBuilderFactory;
	}
	
	@Bean(name="stepBuilderFactory")
	public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, 
				PlatformTransactionManager transactionManager) {
		StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(jobRepository, transactionManager);
		return stepBuilderFactory;
	}
	
}
