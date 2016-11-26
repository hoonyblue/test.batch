package my.job;

import my.service.SampleService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class SampleJob implements Job {
	
	@Autowired(required=true)
	private SampleService service;
	
	public void execute(JobExecutionContext paramJobExecutionContext)
		throws JobExecutionException {
		// todo 서비스 호출
		service.hello();
	
	}

}
