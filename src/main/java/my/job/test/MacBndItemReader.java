package my.job.test;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import my.job.cmn.AbstractItemReader;

@Component("macBndItemReader")
@SuppressWarnings("rawtypes")
@StepScope
public class MacBndItemReader extends AbstractItemReader<Map> {

	@Override
	public List<Map> fetchData() {
		// 로직 작성해야함.
		return null;
	}

}
