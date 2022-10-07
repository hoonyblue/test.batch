package my;

import my.cmn.JobLauncherDetails;
import my.job.SampleJob2;
import my.service.SchedulerService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * JobConfig
 *
 * @author gnsl
 *
 */
@Component
public class JobConfig {

//	@Autowired(required=true)
//	private SchedulerConfig config

//	@Resource(name="schedulerFactoryBean") private SchedulerFactoryBean factory;
	@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
	@Qualifier("schedulerFactoryBean")
	@Autowired private SchedulerFactoryBean factory;
	@Resource(name="schedulerService") private SchedulerService schedulerService;

	private static Logger log = Logger.getLogger(JobConfig.class);

	//@PostConstruct
	public void test() throws SchedulerException {

		log.info(" new job called ");

		JobDetail jobDetail = JobBuilder.newJob(SampleJob2.class)
				.withIdentity(JobKey.jobKey(SampleJob2.class.getName()))
				.storeDurably(true)
				.build();

		log.info("jobname:" + SampleJob2.class.getName());

		Trigger trigger = TriggerBuilder.newTrigger()
		       .forJob(JobBuilder.newJob(JobLauncherDetails.class)
		    		   	.withIdentity(SampleJob2.class.getName())
		    		   	.build())
		       .withIdentity(TriggerKey.triggerKey(SampleJob2.class.getName())) // "testJob"
		       .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
		       .startNow()
		       .build();

		//config.sampleJobTrigger(jobDetail, 2000)
		//Scheduler scheduler = new StdSchedulerFactory().getScheduler()

		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);

		//schedulerService.addJobRegistry("SampleJob")
		//schedulerService.addJobSchedule("SampleJob", "0/30 * * * * ?")
		//schedulerService.addJobSchedule("macBndSyncJob", "0/30 * * * * ?")

		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		log.info (String.format( "size: %d", contexts.size()));
		for (JobExecutionContext context : contexts ) {
			log.info( String.format("%s : %s", context.getJobDetail().getKey().getName(), context.getJobRunTime()));
		}

	}

	@PostConstruct
	public void addJob() throws SchedulerException {

		schedulerService.addJobSchedule("macBndSyncJob", "0/30 * * * * ?");
		Scheduler scheduler = factory.getScheduler();

		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		log.info (String.format( "size: %d", contexts.size()));
		for (JobExecutionContext context : contexts ) {
			log.info( String.format("%s : %s", context.getJobDetail().getKey().getName(), context.getJobRunTime()));
		}

	}


}
