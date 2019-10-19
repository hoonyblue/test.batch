package my.cmn.item.session;

import java.io.IOException;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import my.cmn.utils.couchdbutils.Database;
import my.cmn.utils.couchdbutils.Document;
import my.cmn.utils.couchdbutils.Session;
import my.cmn.utils.couchdbutils.View;
import my.cmn.utils.couchdbutils.ViewResults;

@Component("couchSessionConnector")
//@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="prototype")
@Scope("prototype")
public class CouchSessionConnector {

	//******************************************************
	// * CouchDB 접속 정보
	// ******************************************************
	private String dbName;
	private String dbType;
	private String docName;
	private String viewName;

	//******************************************************
	// CouchDB Session
	//******************************************************
	//@Autowired
	//@Qualifier("couchDBSession")
	@Resource(name="couchDBSession")
	private Session session;

	private static final String DOC_DESIGN_SLSH_SEARCH = "_design/search";

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResults() throws IOException 	{
		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument("_design/"+this.docName);

		return database.view(doc.getView(viewName));
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	public ViewResults getViewResultsFromView() throws IOException {

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);

		return database.view(doc.getView(viewName));
	}

	/**
	 *
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(int limit) throws IOException 	{

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		view.setLimit(limit);

		return database.view(view);
	}

	/**
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String key) throws IOException {

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		view.setKey(key);
		view.setGroup(true);

		return database.view(view);
	}

	/**
	 *
	 * @param key
	 * @param isGroup
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String key,boolean isGroup) throws IOException {

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		view.setKey(key);
		view.setGroup(isGroup);

		return database.view(view);
	}

	/**
	 *
	 * @param startkey
	 * @param endKey
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String startkey, String endKey) throws IOException {
		return getViewResultsFromView(startkey, endKey, true);

	}

	/**
	 *
	 * @param startkey
	 * @param endKey
	 * @param isGroup
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String startkey, String endKey,boolean isGroup) throws IOException {

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		if (view == null) return null; // 2017-07-26 adding NullPointerException 수정.
		view.setStartKey(startkey);
		view.setEndKey(endKey);
		view.setGroup(isGroup);
		//view.setGroupLevel(2)

		return database.view(view);
	}

	/**
	 *
	 * @param startkey
	 * @param endKey
	 * @param isGroup
	 * @param limit
	 * @param skip
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String startkey, String endKey,boolean isGroup, int limit, int skip) throws IOException {

		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		view.setStartKey(startkey);
		view.setEndKey(endKey);
		view.setGroup(isGroup);
		view.setLimit(limit);
		view.setSkip(String.valueOf(skip));
		//view.setGroupLevel(2)
		view.setWithDocs(false);

		return database.view(view);
	}

	/**
	 *
	 * @param startkey
	 * @param endKey
	 * @param groupLevel
	 * @return
	 * @throws Exception
	 */
	public ViewResults getViewResultsFromView(String startkey, String endKey, int groupLevel) throws IOException {
		Database database = session.getDatabase(dbName);
		Document doc = database.getDocument(DOC_DESIGN_SLSH_SEARCH);
		View view = doc.getView(viewName);
		view.setStartKey(startkey);
		view.setEndKey(endKey);
		view.setGroup(true);
		view.setGroupLevel(groupLevel);

		return database.view(view);
	}

	public JSONObject getDatabaseStatus() {
		Database database = session.getDatabase(dbName);
		return database.getStatus();
	}

	/**
	 * 구성정보의 purge를 수행하기 위한 view 호출 메소드
	 *
	 * @return
	 */
	public ViewResults getConfPurgeResults(int limit , int since) throws IOException {

		Database database = session.getDatabase(dbName);
		View view = new View(viewName);

		view.setFilter("deleteddoc/deleted");
		view.setLimit(limit);
		view.setSince(String.valueOf(since));

		return database.view(view);
	}

	public void doPurge(JSONObject json) throws IOException {
		Database database = session.getDatabase(dbName);
		database.purge(json);
	}

	/**
	 *
	 * @throws Exception
	 */
	public void doDatabaseCompact()throws IOException {
		Database database = session.getDatabase(dbName);
		database.compact();
	}

	/**
	 *
	 * @throws Exception
	 */
	public void doViewCompact()throws IOException {
		Database database = session.getDatabase(dbName);
		database.compact(viewName);
	}

	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * @return the couchSession
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param couchSession the couchSession to set
	 */
	public void setSession(Session couchSession) {
		this.session = couchSession;
	}

}
