package my.service.impl;

import lombok.extern.slf4j.Slf4j;
import my.service.SampleService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleServiceImpl implements SampleService {

	public void hello() {
		log.info("Hello world!!");
		//System.out.println(DateFormatUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss") + " Hello world!!");
	}
}
