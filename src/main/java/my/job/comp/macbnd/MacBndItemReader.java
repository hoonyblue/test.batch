package my.job.comp.macbnd;

import java.util.List;
import java.util.Map;

import my.job.comp.macbnd.vo.CfMacBndVO;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import my.job.cmn.AbstractItemReader;

@Component("macBndItemReader")
@SuppressWarnings("rawtypes")
@StepScope
public class MacBndItemReader extends AbstractItemReader<CfMacBndVO> {

	@Override
	public List<CfMacBndVO> fetchData() {
		// 로직 작성해야함.
		return null;
	}

}
