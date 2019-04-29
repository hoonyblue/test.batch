package my;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import my.job.SampleJob2;
import my.service.SchedulerService;

/**
 * 
 * @author gnsl
 *
 */
@Component
public class JobSetting {

//	@Autowired(required=true)
//	private SchedulerConfig config;
	
	@Autowired(required=true)
	private SchedulerFactoryBean factory;
	@Autowired
	private SchedulerService schedulerService; 
	
	private static Logger log = Logger.getLogger(JobSetting.class);	
	
	@PostConstruct
	public void test() throws Exception {
		
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " new job called ");
		log.info(" new job called ");
		
		
		
		JobDetail jobDetail = JobBuilder.newJob(SampleJob2.class)
				.withIdentity(JobKey.jobKey(SampleJob2.class.getName()))
				.storeDurably(true)
				.build();
		
//		Trigger trigger = TriggerBuilder.newTrigger()
//				.forJob(jobDetail)
//				.startNow()
//				.build();
		
		log.info("jobname:" + SampleJob2.class.getName());
		
		Trigger trigger = TriggerBuilder.newTrigger()
		       .forJob(jobDetail)
		       .withIdentity(TriggerKey.triggerKey(SampleJob2.class.getName())) // "testJob"
		       .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
		       .startNow()
		       .build();

//		schedulerService.addJobSchedule("SampleJob2", "0/30 * * * * ?");
		schedulerService.addJobSchedule("macBndSyncJob", "0/30 * * * * ?");
		

		//config.sampleJobTrigger(jobDetail, 2000);
		
		//Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		//scheduler.getContext().
		log.info (String.format( "size: %d", contexts.size()));
		for (JobExecutionContext context : contexts ) {
			log.info( String.format("%s : %s", context.getJobDetail().getKey().getName(), context.getJobRunTime()));
		}
		
	}
	
	
}
