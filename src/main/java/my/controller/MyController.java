package my.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import my.job.SampleJob2;
import my.service.SchedulerService;

import javax.annotation.Resource;

/**
 * 테스트 Rest Controller
 *
 * @author yhlee
 */
@RestController
public class MyController {

	@Resource(name="schedulerService") SchedulerService service;

	private static Logger log = Logger.getLogger(MyController.class);

	@GetMapping("/")
	public @ResponseBody String index() {

		log.info("test");

		try {
			service.removeJob(SampleJob2.class.getName()); //"SampleJob2"
			log.info("del job");
			String addjob = service.addJob("my.job.SampleJob2", SampleJob2.class.getName(), "0/10 * * * * ?");
			log.info("add job: " + addjob);

			service.test();

		} catch(Exception e) {
			log.error("error", e);
		}

		return "Hello Woniper Spring Boot~";
	}

}
