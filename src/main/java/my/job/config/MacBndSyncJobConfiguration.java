package my.job.config;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import my.job.comp.macbnd.MacBndItemProcessor;
import my.job.comp.macbnd.MacBndItemReader;
import my.job.comp.macbnd.MacBndItemWriter;
import my.job.comp.macbnd.vo.CfMacBndVO;
import my.job.comp.macbnd.vo.MacBnd;

//@Slf4j
@Configuration
@EnableBatchProcessing
public class MacBndSyncJobConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Resource(name="macBndItemReader")
	private MacBndItemReader macBndItemReader;
	@Resource(name="macBndItemProcessor")
	private MacBndItemProcessor macBndItemProcessor;
	@Resource(name="macBndItemWriter")
	private MacBndItemWriter macBndItemWriter;

	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final String FAILED = "FAILED";

	@Bean
	public Job macBndSyncJob() {
		return jobBuilderFactory.get("macBndSyncJob")
				.start(macBndSyncJobDotMacBndSyncStep())
					.on(FAILED)
					.fail()
				.from(macBndSyncJobDotMacBndSyncStep())
					.on("*")
					.to(macBndSyncJobDotDeletedMacBndDataSyncStep())
					.end()
				.build();
	}

	@Bean
//	@StepScope
	public Step macBndSyncJobDotMacBndSyncStep() {
		return stepBuilderFactory.get("macBndSyncJobDotMacBndSyncStep")
/**
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>>>> This is macBndSyncJobDotMacBndSyncStep");
					// Need logic
					contribution.setExitStatus(ExitStatus.FAILED);
					return RepeatStatus.FINISHED;
				})
 */
				.<CfMacBndVO, MacBnd> chunk(2)
					.reader(macBndItemReader)
					.processor(macBndItemProcessor)
					.writer(macBndItemWriter)
					/**
					.processor(item -> {
						return new MacBnd();
					})
					.writer(items -> {
						for (MacBnd item : items) {
							log.info("writer.item: {}", item);
						}
					})
					 */
				.build();
	}

	@Bean
//	@StepScope
	public Step macBndSyncJobDotDeletedMacBndDataSyncStep() {
		return stepBuilderFactory.get("macBndSyncJobDotDeletedMacBndDataSyncStep")
				.chunk(10000)
					.reader(() -> {
						return null;
					})
//					.processor(null)
					.writer(items -> {
						for (Object item : items) {
							log.info("writer.item: {}", item);
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
