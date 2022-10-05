package my.cmn.item;

import static org.springframework.util.Assert.notNull;

import javax.annotation.Resource;

import my.cmn.item.session.CouchSessionConnector;

public abstract class AbstractCouchItemReader<T> extends AbstractItemReader<T> {

	//@Autowired
	//@Qualifier("couchSessionConnector")
	@Resource(name="couchSessionConnector")
	protected CouchSessionConnector couchSessionConnector;

	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(couchSessionConnector.getDbName(), "Couch DB Name is NULL..............");
		/**
		notNull(couchSessionConnector.getDbType(), "Couch DB Type is NULL..............");
		notNull(couchSessionConnector.getDocName(), "Couch DB Doc Name is NULL..............");
		notNull(couchSessionConnector.getViewName(), "Couch DB View Name is NULL..............");
		 */
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		couchSessionConnector.setDbName(dbName);
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		couchSessionConnector.setDbType(dbType);
	}

	/**
	 * @param docName the docName to set
	 */
	public void setDocName(String docName) {
		couchSessionConnector.setDocName(docName);
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		couchSessionConnector.setViewName(viewName);
	}

}
