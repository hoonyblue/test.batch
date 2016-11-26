package my.controller;

import my.job.SampleJob2;
import my.service.SchedulerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
	
	@Autowired
	SchedulerService service;

	private static Logger log = Logger.getLogger(MyController.class);
	
	@RequestMapping("/")
	public @ResponseBody String index() {
		
		log.info("test");
		
		try {
			service.delJob(SampleJob2.class.getName()); //"SampleJob2");
			log.info("del job");
			String _addjob = service.addJob("my.job.SampleJob2", SampleJob2.class.getName(), "0/10 * * * * ?");
			log.info("add job: " + _addjob);
			
			service.test();
			
		} catch(Exception e) {
			log.error("error", e);
		}
		
		return "Hello Woniper Spring Boot~";
	}
	
}
