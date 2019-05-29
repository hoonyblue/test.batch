package my.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import my.job.SampleJob2;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import egovframework.rte.bat.sample.scheduler.support.EgovJobLauncherDetails;

/**
 * 
 * @author 이용훈
 *
 */
@Service
public class SchedulerService {
	
	@Autowired(required=true)
	private SchedulerFactoryBean factory;

	private static Logger log = Logger.getLogger(SampleJob2.class);
	
	/**
	 * job 추가
	 * @param jobClass
	 * @param jobId
	 * @param cronexpr
	 * @throws Exception
	 */
	public <T> String addJob(String jobClass, String jobId, String cronexpr) throws Exception {
		log.info("addJob : " + jobId);
			
//		Trigger trigger = TriggerBuilder.newTrigger()
//		       .forJob(jobDetail)
//		       .withIdentity(jobId) //"testJob")
//		       .withSchedule(CronScheduleBuilder.cronSchedule(cronexpr)) //"0/5 * * * * ?"))
//		       .startNow()
//		       .build();

		@SuppressWarnings("unchecked")
		JobDetail jobDetail = this.newJob((Class<? extends Job>) Class.forName(jobClass)); //SampleJob2.class);
		Trigger trigger = this.newTrigger(jobDetail, jobId, cronexpr);

		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		
		//currentJob(); 
		
		return jobDetail.getKey().getName();
	}
	
	/**
	 * job 삭제
	 * @param jobId
	 * @throws Exception
	 */
	public void removeJob(String jobId) throws Exception {
		log.info("delJob : " + jobId);
		
		Scheduler scheduler = factory.getScheduler();
		log.info("check_job:" +  scheduler.checkExists(JobKey.jobKey(jobId)));
		scheduler.deleteJob(JobKey.jobKey(jobId));
		log.info("check_trigger:" +  scheduler.checkExists(TriggerKey.triggerKey(jobId)));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobId)); 
	}

	/**
	 * new job
	 * @param jobClass
	 * @return
	 */
	private JobDetail newJob(Class<? extends Job> jobClass) {
		return JobBuilder.newJob(jobClass)				
				.storeDurably(true)
				.withIdentity(JobKey.jobKey(jobClass.getName()))
				.build();
	}
	
	/**
	 * new trigger
	 * @param jobDetail
	 * @param jobId
	 * @param cronexpr
	 * @return
	 */
	private Trigger newTrigger(JobDetail jobDetail, String jobId, String cronexpr) {
		return TriggerBuilder.newTrigger()
			       .forJob(jobDetail)
			       .withIdentity(TriggerKey.triggerKey(jobId))
			       .withSchedule(CronScheduleBuilder.cronSchedule(cronexpr))
			       .startNow()
			       .build();		
	}
	
	@SuppressWarnings("unused")
	private void currentJob() throws Exception {
		Scheduler scheduler = factory.getScheduler();
		
		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		log.info ("size: " + contexts.size());
		for (JobExecutionContext context : contexts ) {
			log.info( context.getJobDetail().getKey().getName() + ": " + context.getJobRunTime());
		}		
	}
	
	@Autowired
	private JobRegistry jobRegistry; // MapJobRegistry
	@Autowired
	private JobLauncher jobLauncher; // SimpleJobLauncher
	@Autowired
	private JobRepository jobRepository; //JobRepositoryFactoryBean
	
	/**
	 * 테스트 모듈..
	 * @ses egov batch job + quartz(스케쥴)
	 * @throws Exception
	 */
	public void test() throws Exception {
		
		String jobName = "ibatisToDelimitedJob";
		String cronExpr = "0/10 * * * * ?";
		
		addJobSchedule(jobName, cronExpr);
		
//		JobDetailFactoryBean jobfactory = new JobDetailFactoryBean();
//		jobfactory.setJobClass(EgovJobLauncherDetails.class);
//		jobfactory.setGroup("quartz-batch");
//		jobfactory.setName("ibatisToDelimitedJob");
//		
//		Map<String, Object> jobDataAsMap = new Hashtable<String, Object>();
//		jobDataAsMap.put("jobName", jobName);
//		jobDataAsMap.put("jobLocator", jobRegistry);
//		jobDataAsMap.put("jobLauncher", jobLauncher);
//		jobfactory.setJobDataAsMap(jobDataAsMap);
//		jobfactory.afterPropertiesSet();
//		JobDetail jobDetail = jobfactory.getObject();
//		log.info(String.format("jobDetail : %s", jobDetail));
//		log.info(String.format("jobName : %s", jobName));
//		
//		org.springframework.batch.core.job.SimpleJob job = new org.springframework.batch.core.job.SimpleJob(jobName);
//		job.setJobRepository((JobRepository) jobRepository);
//		ReferenceJobFactory jobFactory = new ReferenceJobFactory((org.springframework.batch.core.Job)job);
//		jobRegistry.register(jobFactory);
//		
//		Trigger trigger = newTrigger(jobDetail, jobName, cronExpr);		
//		Scheduler scheduler = factory.getScheduler();
//		scheduler.scheduleJob(jobDetail, trigger);
		
	}
	
	/**
	 * job scheduler 등록. (egov batch)
	 * @param jobId
	 * @param cronExpr <expression :  예) 0/10 * * * * ?  => 10초마다. 
	 * @throws Exception
	 */
	public void addJobSchedule(String jobId, String cronExpr) throws Exception {

		JobDetailFactoryBean jobfactory = new JobDetailFactoryBean();
		jobfactory.setJobClass(EgovJobLauncherDetails.class);
		jobfactory.setGroup("quartz-batch");
		jobfactory.setName(jobId);

		Map<String, Object> jobDataAsMap = new Hashtable<String, Object>();
		jobDataAsMap.put("jobName", jobId);
		jobDataAsMap.put("jobLocator", jobRegistry);
		jobDataAsMap.put("jobLauncher", jobLauncher);
		jobfactory.setJobDataAsMap(jobDataAsMap);
		jobfactory.afterPropertiesSet();
		JobDetail jobDetail = jobfactory.getObject();
		log.debug(String.format("jobDetail : %s", jobDetail));
		log.debug(String.format("jobName : %s", jobId));
		
		org.springframework.batch.core.job.SimpleJob job = new org.springframework.batch.core.job.SimpleJob(jobId);
		job.setJobRepository((JobRepository) jobRepository);
		ReferenceJobFactory jobFactory = new ReferenceJobFactory((org.springframework.batch.core.Job)job);
		jobRegistry.register(jobFactory);
		
		Trigger trigger = newTrigger(jobDetail, jobId, cronExpr);		
		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);

	}
	
	public void removeJobSchedule(String jobId) throws Exception {

		Scheduler scheduler = factory.getScheduler();
		boolean _chk = scheduler.checkExists(JobKey.jobKey(jobId));
		log.debug(String.format("check_job: %s",_chk));
		if (_chk) {
			scheduler.deleteJob(JobKey.jobKey(jobId));
		} else {
			log.info(String.format("jobId: %s is not existed.", jobId));
		}
		boolean _chk2 = scheduler.checkExists(TriggerKey.triggerKey(jobId));
		log.debug(String.format("check_trigger:", _chk2));
		if (_chk2) 
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobId));
		// jobRegistry 제거.
		jobRegistry.unregister(jobId);
	}
	
	/**
	 * trigger 갱신 (삭제 후, 재등록)
	 * @param jobId
	 * @param cronExpr
	 * @throws Exception
	 */
	public void updateJobSchedule(String jobId, String cronExpr) throws Exception {
		removeJobSchedule(jobId);
		addJobSchedule(jobId, cronExpr);	
		
	}
}
