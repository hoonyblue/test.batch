package my;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@Import({SchedulerConfig.class})
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class})
@ImportResource("classpath:/batch/context-*.xml")
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		
		//new JobSetting().test();
//		List<String> paths = new ArrayList<String>();
//		paths.add("/batch/context-scheduler-job.xml");
//		paths.add("/batch/context-batch-job-launcher.xml");
//		paths.add("/batch/context-batch-sqlmap.xml");
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(locations, false);
				
	}
	
}
