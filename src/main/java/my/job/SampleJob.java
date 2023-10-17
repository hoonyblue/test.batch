package my.job;

import my.service.SampleService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleJob implements Job {
	
	@Autowired
	private SampleService service;
	
	public void execute(JobExecutionContext paramJobExecutionContext)
		throws JobExecutionException {
		// todo 
		service.hello();
	
	}

}
