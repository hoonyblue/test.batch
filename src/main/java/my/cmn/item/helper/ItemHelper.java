package my.cmn.item.helper;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;

public class ItemHelper {

	protected StepExecution stepExecution;

	@BeforeStep
   	public void beforeStep(StepExecution stepExecution) {
   		this.stepExecution = stepExecution;
   	}

	/**
	 * JOB 전체에서 사용될 항목을 저장함.
	 * @param key
	 * @param source
	 */
	public void setStepParameter(String key, Object source) {
		stepExecution.getExecutionContext().put(key, source);
	}

	public Object getStepParameter(String key) {
		return stepExecution.getExecutionContext().get(key);
	}
}
