package my.apps.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import egovframework.rte.bat.sample.scheduler.support.EgovJobLauncherDetails;

@Configuration
public class SchedulerJobConfig {

	@Bean(name="jobDetail")
	public JobDetailFactoryBean jobDetail(JobRegistry jobRegistry, JobLauncher jobLauncher) {
		JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
		jobDetail.setJobClass(EgovJobLauncherDetails.class);
		jobDetail.setGroup("quartz-batch");
		Map<String, Object> jobDataAsMap = new HashMap<String, Object>();
		jobDataAsMap.put("jobName", "ibatisToDelimitedJob");
		jobDataAsMap.put("jobLauncher", jobLauncher);
		jobDataAsMap.put("jobLocator", jobRegistry);
		jobDetail.setJobDataAsMap(jobDataAsMap);
		
		return jobDetail;
	}
}
