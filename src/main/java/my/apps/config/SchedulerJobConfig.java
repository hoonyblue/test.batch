package my.apps.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import my.cmn.JobLauncherDetails;

@Configuration
public class SchedulerJobConfig {

	@Bean(name="jobDetail")
	public JobDetailFactoryBean jobDetail(JobRegistry jobRegistry, JobLauncher jobLauncher) {
		JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
		jobDetail.setJobClass(JobLauncherDetails.class);
		jobDetail.setGroup("quartz-batch");
		Map<String, Object> jobDataAsMap = new HashMap<>();
		jobDataAsMap.put("jobName", "ibatisToDelimitedJob");
		jobDataAsMap.put("jobLauncher", jobLauncher);
		jobDataAsMap.put("jobLocator", jobRegistry);
		jobDetail.setJobDataAsMap(jobDataAsMap);

		return jobDetail;
	}
}
