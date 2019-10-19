package my.cmn.item;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

import my.cmn.item.helper.ItemHelper;

public abstract class AbstractItemProcessor<K, T> extends ItemHelper implements ItemProcessor<K, T>, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
