package my.job.config;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import my.job.test.MacBndItemReader;

//@Slf4j
@Configuration
@EnableBatchProcessing
public class MacBndSyncJobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	private final Log log = LogFactory.getLog(getClass());
	
	private static final String FAILED = "FAILED";
	
	@Autowired
	private MacBndItemReader macBndItemReader;
	
	@Bean
	public Job macBndSyncJob() {
		return (Job) jobBuilderFactory.get("macBndSyncJob")
				.start(macBndSyncJob_MacBndSyncStep())
					.on(FAILED)
					.fail()
				.from(macBndSyncJob_MacBndSyncStep())
					.on("*")
					.to(macBndSyncJob_deletedMacBndDataSyncStep())
					.end()
				.build();
	}
	
	@SuppressWarnings("rawtypes")
	@Bean
//	@StepScope
	public Step macBndSyncJob_MacBndSyncStep() {
		return stepBuilderFactory.get("macBndSyncJob_MacBndSyncStep")
//				.tasklet((contribution, chunkContext) -> {
//					log.info(">>>>>>> This is macBndSyncJob_MacBndSyncStep");
//					// TODO logic
//					contribution.setExitStatus(ExitStatus.FAILED);
//					return RepeatStatus.FINISHED;
//				})
				.chunk(1)
					.reader(macBndItemReader)
					.processor((item) -> {
						return new HashMap();
					})
					.writer((items) -> {
						for (Object item : items) {
							System.out.println(String.format("item: %s", item));
						}
					})
				.build();
	}
	
	@Bean
//	@StepScope
	public Step macBndSyncJob_deletedMacBndDataSyncStep() {
		return stepBuilderFactory.get("macBndSyncJob_deletedMacBndDataSyncStep")
				.chunk(10000)
					.reader(() -> {
						return null;
					})
//					.processor(null)
					.writer((items) -> {
						for (Object item : items) {
							System.out.println(String.format("item: %s", item));
						}
					})
					.exceptionHandler((context, throwable) -> {
						log.debug(String.format("context: %s, throwable: %s", context, throwable));
						if (throwable instanceof java.lang.Exception) {
						}
					})
				.build();
		
	}
}
