package my.job.cmn;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public abstract class AbstractItemReader<T> implements ItemReader<T> {

	private List<T> data;
	private boolean init = false;
	private int nextIndex;
	
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException, 
		NonTransientResourceException {
		
		if (isNotInitialized()) {
			fetchData();
		}
		T nextData = null;
		
		if (data != null) {
			if (nextIndex <data.size()) {
				nextData = data.get(nextIndex);
				nextIndex++;
			}
		}
		return nextData;
	}

	private boolean isNotInitialized() {
		return init;
	}
	
	public abstract List<T> fetchData();
}
