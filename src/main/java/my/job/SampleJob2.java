package my.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class SampleJob2 implements Job {
	
	public void execute(JobExecutionContext paramJobExecutionContext)
		throws JobExecutionException {
	
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " SampleJob2 called");
		log.info("SampleJob2 called");
	}

}
