package my.cmn.item;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import my.cmn.item.helper.ItemHelper;

public abstract class AbstractItemWriter<T> extends ItemHelper implements ItemWriter<T>, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {}

	/**
	@Override
	public abstract void write(List<? extends T> list) throws Exception;
	 */

}
