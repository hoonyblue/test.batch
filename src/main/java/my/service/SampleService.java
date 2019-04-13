package my.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
	private static Logger log = Logger.getLogger(SampleService.class);
	
	public void hello() {
		log.info("Hello world!!");
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " Hello world!!");
	}
}
