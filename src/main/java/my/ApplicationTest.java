package my;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import my.job.SampleJob;


@SpringBootTest(classes=Application.class)
public class ApplicationTest {

	@Autowired
	private Scheduler scheduler; 
	
	@Test
	public void test() throws Exception {
		
		JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
				.storeDurably(true)
				.build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.startNow()
				.build();
		
		scheduler.scheduleJob(jobDetail, trigger);
		
		Thread.sleep(5000);
		
	}
	
}
