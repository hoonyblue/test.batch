package my.service.impl;

import my.service.SampleService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService {
	private static Logger log = Logger.getLogger(SampleServiceImpl.class);
	
	public void hello() {
		log.info("Hello world!!");
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " Hello world!!");
	}
}
