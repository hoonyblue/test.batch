package my.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import my.job.test.MacBndItemReader;

@Slf4j
@Configuration
@EnableBatchProcessing
public class MacBndSyncJobConfiguration {

	/**
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	 */
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
//	private final Log log = LogFactory.getLog(getClass())

	private static final String FAILED = "FAILED";
	/**
	@Autowired
	private MacBndItemReader macBndItemReader;
	 */

	@Bean
	public Job macBndSyncJob(JobBuilderFactory jobBuilderFactory, MacBndItemReader macBndItemReader) {
		return jobBuilderFactory.get("macBndSyncJob")
				.start(macBndSyncJobMacBndSyncStep(macBndItemReader))
					.on(FAILED)
					.fail()
				/**
				.from(macBndSyncJobMacBndSyncStep(macBndItemReader))
					.on("*")
					.to(macBndSyncJobDeletedMacBndDataSyncStep()) */
				.next(macBndSyncJobDeletedMacBndDataSyncStep())
					.end()
				.build();
	}

	@Bean
	//@StepScope
	public Step macBndSyncJobMacBndSyncStep(MacBndItemReader macBndItemReader) {
		return stepBuilderFactory.get("macBndSyncJob_MacBndSyncStep")
				/**
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>>>> This is macBndSyncJob_MacBndSyncStep");
					contribution.setExitStatus(ExitStatus.FAILED);
					return RepeatStatus.FINISHED;
				}) */
				.chunk(1)
					.reader(macBndItemReader)
					.processor(processor())
					.writer(writer())
					.faultTolerant()
					.skip(java.lang.Exception.class)
					.skipLimit(100)
				.build();
	}

	@Bean
	//@StepScope
	public Step macBndSyncJobDeletedMacBndDataSyncStep() {
		return stepBuilderFactory.get("macBndSyncJob_deletedMacBndDataSyncStep")
				.chunk(10000)
					.reader(() -> null)
//					.processor(null)
					.writer(items -> {
						for (Object item : items) {
							log.debug("item: {}", item);
						}
					})
					.exceptionHandler((context, throwable) -> { // NOSONAR
						log.debug(String.format("context: %s, throwable: %s", context, throwable));
						/**
						if (throwable instanceof java.lang.Exception) {
						} */
					})
				.build();

	}

	@Bean
	public ItemReader<Object> reader() {
		return () -> {
			log.debug("item reader...");
			return null;
		};
	}

	@Bean
	public ItemProcessor<Object, Object> processor() {
		return item -> {
			log.debug("item processor...");
			return null;
		};
	}

	@Bean
	public ItemWriter<Object> writer() {
		return items -> {
			for (Object item : items) {
				//System.out.println(String.format("item: %s", item))
				//log.debug(String.format("item: %s", item))
				log.debug("item: {}", item);
			}
		};
	}
}
