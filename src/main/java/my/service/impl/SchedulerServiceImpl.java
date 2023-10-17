package my.service.impl;

import lombok.extern.slf4j.Slf4j;
import my.cmn.JobLauncherDetails;
import my.service.SchedulerService;
import org.quartz.*;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 스케줄러 서비스
 *
 * @author yhlee
 *
 */
@Slf4j
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	//@Resource(name="schedulerFactoryBean") private SchedulerFactoryBean factory;
	@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
	@Qualifier("schedulerFactoryBean")
	@Autowired private SchedulerFactoryBean factory;

	@Resource(name="jobRegistry") private JobRegistry jobRegistry; // MapJobRegistry
	@Resource(name="jobLauncher")  private JobLauncher jobLauncher; // SimpleJobLauncher
	@Resource(name="jobRepository") private JobRepository jobRepository; //JobRepositoryFactoryBean

	/**
	 * job 추가
	 * @param jobClass 잡 클래스
	 * @param jobId 잡ID
	 * @param cronexpr 크론표현식
	 */
	public String addJob(String jobClass, String jobId, String cronexpr) throws ClassNotFoundException, SchedulerException {
		log.info("addJob : " + jobId);

		/*
		Trigger trigger = TriggerBuilder.newTrigger()
		       .forJob(jobDetail)
		       .withIdentity(jobId) //"testJob")
		       .withSchedule(CronScheduleBuilder.cronSchedule(cronexpr)) //"0/5 * * * * ?"))
		       .startNow()
		       .build();
		 */

		@SuppressWarnings("unchecked")
		JobDetail jobDetail = this.newJob((Class<? extends Job>) Class.forName(jobClass)); //SampleJob2.class
		Trigger trigger = this.newTrigger(jobDetail, jobId, cronexpr);

		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);

		//currentJob()

		return jobDetail.getKey().getName();
	}

	/**
	 * job 삭제
	 * @param jobId 잡ID
	 */
	public void removeJob(String jobId) throws SchedulerException {
		log.info("delJob : " + jobId);

		Scheduler scheduler = factory.getScheduler();
		log.info("check_job:" +  scheduler.checkExists(JobKey.jobKey(jobId)));
		scheduler.deleteJob(JobKey.jobKey(jobId));
		log.info("check_trigger:" +  scheduler.checkExists(TriggerKey.triggerKey(jobId)));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobId));
	}

	/**
	 * new job
	 * @param jobClass 잡클래스
	 * @return {@code JobDetail}
	 */
	private JobDetail newJob(Class<? extends Job> jobClass) {
		return JobBuilder.newJob(jobClass)
				.storeDurably(true)
				.withIdentity(JobKey.jobKey(jobClass.getName()))
				.build();
	}

	/**
	 * new trigger
	 * @param jobDetail 잡디테일
	 * @param jobId 잡ID
	 * @param cronexpr 크론표현식
	 * @return {@code Trigger}
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
	private void currentJob() throws SchedulerException {
		Scheduler scheduler = factory.getScheduler();

		List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
		log.info ("size: " + contexts.size());
		for (JobExecutionContext context : contexts ) {
			log.info( context.getJobDetail().getKey().getName() + ": " + context.getJobRunTime());
		}
	}

	/**
	 * 테스트 모듈..
	 *  <p>- egov batch job + quartz(스케쥴)
	 */
	public void test() throws SchedulerException {

		String jobName = "ibatisToDelimitedJob";
		String cronExpr = "0/10 * * * * ?";

		// function 분리
		addJobSchedule(jobName, cronExpr);
	}

	/**
	 * job scheduler 등록. (egov batch)
	 * @param jobId 잡ID
	 * @param cronExpr <expression :  예) 0/10 * * * * ?  => 10초마다.
	 */
	public void addJobSchedule(String jobId, String cronExpr) throws SchedulerException {

		JobDetailFactoryBean jobfactory = new JobDetailFactoryBean();
		jobfactory.setJobClass(JobLauncherDetails.class);
		jobfactory.setGroup("quartz-batch");
		jobfactory.setName(jobId);

		Map<String, Object> jobDataAsMap = new HashMap<>();
		jobDataAsMap.put("jobName", jobId);
		jobDataAsMap.put("jobLocator", jobRegistry);
		jobDataAsMap.put("jobLauncher", jobLauncher);
		jobfactory.setJobDataAsMap(jobDataAsMap);
		jobfactory.afterPropertiesSet();
		JobDetail jobDetail = jobfactory.getObject();
		log.debug(String.format("jobDetail : %s", jobDetail));
		log.debug(String.format("jobName : %s", jobId));

		/*
		org.springframework.batch.core.job.SimpleJob job = new org.springframework.batch.core.job.SimpleJob(jobId);
		job.setJobRepository((JobRepository) jobRepository);
		ReferenceJobFactory jobFactory = new ReferenceJobFactory((org.springframework.batch.core.Job)job);
		jobRegistry.register(jobFactory);
		 */

		Trigger trigger = newTrigger(jobDetail, jobId, cronExpr);
		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);

	}

	@SuppressWarnings("unused")
	public void addJobRegistry(String jobId) throws DuplicateJobException {
		SimpleJob job = new SimpleJob(jobId);
		job.setJobRepository(jobRepository);
		ReferenceJobFactory jobFactory = new ReferenceJobFactory(job);
		jobRegistry.register(jobFactory);
	}

	public void removeJobSchedule(String jobId) throws SchedulerException {

		Scheduler scheduler = factory.getScheduler();
		boolean chk = scheduler.checkExists(JobKey.jobKey(jobId));
		log.debug(String.format("check_job: %s",chk));
		if (chk) {
			scheduler.deleteJob(JobKey.jobKey(jobId));
		} else {
			log.info(String.format("jobId: %s is not existed.", jobId));
		}
		boolean chk2 = scheduler.checkExists(TriggerKey.triggerKey(jobId));
		log.debug(String.format("check_trigger: %s", chk2));
		if (chk2) {
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobId));
		}
		// jobRegistry 제거.
		jobRegistry.unregister(jobId);
	}

	/**
	 * trigger 갱신 (삭제 후, 재등록)
	 * @param jobId 잡ID
	 * @param cronExpr 크론표현식
	 */
	@SuppressWarnings("unused")
	public void updateJobSchedule(String jobId, String cronExpr) throws SchedulerException {
		removeJobSchedule(jobId);
		addJobSchedule(jobId, cronExpr);

	}

}
