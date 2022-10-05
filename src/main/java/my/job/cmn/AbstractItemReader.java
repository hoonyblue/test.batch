package my.job.cmn;

import java.util.List;

import org.springframework.batch.item.ItemReader;

public abstract class AbstractItemReader<T> implements ItemReader<T> {

	private List<T> data;
	private boolean init = false;
	private int nextIndex;

	@Override
	public T read() throws Exception {

		if (isNotInitialized()) {
			fetchData();
		}
		T nextData = null;

		if (data != null && nextIndex <data.size()) {
			nextData = data.get(nextIndex);
			nextIndex++;
		}
		return nextData;
	}

	private boolean isNotInitialized() {
		return init;
	}

	public abstract List<T> fetchData();
}
