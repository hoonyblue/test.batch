package my.cmn.item;

import java.util.List;

import org.springframework.batch.item.ItemReader;

public abstract class AbstractItemReader<T> implements ItemReader<T> { //, InitializingBean

	private List<T> data;
	private int nextIndex;
	//private boolean init = false

	@Override
	public T read() throws Exception {

		if (isNotInitialized()) {
			data = fetchData();
		}
		T nextData = null;

		if (data != null && nextIndex < data.size()) {
			nextData = data.get(nextIndex);
			nextIndex++;
		}
		return nextData;
	}

	private boolean isNotInitialized() {
		return data == null;
	}

	public abstract List<T> fetchData() throws Exception;

	public void afterPropertiesSet() throws Exception {
	}
}
