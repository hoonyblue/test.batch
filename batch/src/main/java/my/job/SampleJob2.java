package my.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SampleJob2 implements Job {
	
	private static Logger log = Logger.getLogger(SampleJob2.class);
	
	public void execute(JobExecutionContext paramJobExecutionContext)
		throws JobExecutionException {
	
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " SampleJob2 called");
		log.info("SampleJob2 called");
	}

}
