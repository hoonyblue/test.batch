package my.apps.config;

import lombok.extern.slf4j.Slf4j;
import my.job.SampleJob;
import my.spring.AutowireSpringBeanJobFactory;
import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;

/**
 * 스케줄러 Config
 *
 * @author yhlee
 *
 */
@Slf4j
@Configuration
public class SchedulerConfig {

	private static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		AutowireSpringBeanJobFactory jobFactory = new AutowireSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

    @Bean
    public JobDetailFactoryBean sampleJobDetail() {
        return createJobDetail(SampleJob.class);
    } // SampleJob.class
    private static JobDetailFactoryBean createJobDetail(Class<SampleJob> jobClass) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean(name="sampleJobTrigger")
  //public SimpleTriggerFactoryBean sampleJobTrigger(@Qualifier("sampleJobDetail") JobDetail jobDetail,
    public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("sampleJobDetail") JobDetail jobDetail,
                                                   @Value("${samplejob.frequency}") long frequency) {
        //return createTrigger(jobDetail, frequency)
        return createCronTrigger(jobDetail, "0/5 * * * * ?");
    }
    // Use this method for creating cron triggers instead of simple triggers:
    private static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

        log.debug("{} createTrigger called {}, cronExpression: {}", DateFormatUtils.format(new Date(), DATE_FORMAT_YYYYMMDDHHMMSS), jobDetail, cronExpression);
        return factoryBean;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory /*DataSource dataSource,*/
            , @Qualifier("sampleJobTrigger") Trigger sampleJobTrigger) {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setTriggers(sampleJobTrigger);

        //factory.setDataSource(dataSource)
        //factory.setQuartzProperties(quartzProperties())

        log.debug("{} schedulerFactoryBean 초기화", DateFormatUtils.format(new Date(), DATE_FORMAT_YYYYMMDDHHMMSS));

        return schedulerFactoryBean;
    }


//    @SuppressWarnings("unused")
//	private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
//        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
//        factoryBean.setJobDetail(jobDetail);
//        factoryBean.setStartDelay(0L);
//        factoryBean.setRepeatInterval(pollFrequencyMs);
//        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
//        // in case of misfire, ignore all missed triggers and continue :
//        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//
//        //System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " createTrigger called "+  jobDetail + ", pollFrequencyMs:" + pollFrequencyMs)
//        log.debug("{} createTrigger called {}, pollFrequencyMs: {}", DateFormatUtils.format(new Date(), DATE_FORMAT_YYYYMMDDHHMMSS), jobDetail, pollFrequencyMs);
//        return factoryBean;
//    }

//     //	@Bean
//     public Properties quartzProperties() throws IOException {
//     PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//     propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//     propertiesFactoryBean.afterPropertiesSet();
//     return propertiesFactoryBean.getObject();
//     }

}
