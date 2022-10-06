/*
   Copyright 2007 Fourspaces Consulting, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package my.cmn.utils.couchdbutils;

import static my.cmn.utils.couchdbutils.JSONUtils.urlEncodePath;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This represents a particular database on the CouchDB server
 * <p/>
 * Using this object, you can get/create/update/delete documents. You can also
 * call views (named and adhoc) to query the underlying database.
 *
 * @author mbreese
 */
public class Database {
	Log log = LogFactory.getLog(Database.class);
	private final String name;
	private int documentCount;
	private int updateSeq;
	@SuppressWarnings("unused")
	private int purgeSeq;
	private Session session;

	private static final String VIEW = "/_view/";
	private static final String DESIGN = "_design/";
	@SuppressWarnings("unused")
	private static final String UPDATE = "/_update/";
	private static final String PURGE = "/_purge/";
	private static final String ALL_DOCS = "_all_docs";


	/**
	 * C-tor only used by the Session object. You'd never call this directly.
	 *
	 * @param json
	 * @param session
	 */
	Database(JSONObject json, Session session) throws JSONException {
		name = json.getString("db_name");
		documentCount = json.getInt("doc_count");
		this.session = session;
	}

	/**
	 * The name of the database
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * The number of documents in the database <b>at the time that it was
	 * retrieved from the session</b> This number probably isn't accurate after
	 * the initial load... so if you want an accurate assessment, call
	 * Session.getDatabase() again to reload a new database object.
	 *
	 * @return
	 */
	public int getDocumentCount() {
		return documentCount;
	}

	/**
	 * The update seq from the initial database load. The update sequence is the
	 * 'revision id' of an entire database. Useful for getting all documents in
	 * a database since a certain revision
	 *
	 * @return
	 * @see getAllDocuments()
	 */
	public int getUpdateSeq() {
		return updateSeq;
	}

	/**
	 * Runs the standard "_all_docs" view on this database
	 *
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults getAllDocuments() throws IOException {
		return view(new View(ALL_DOCS), false);
	}

	/**
	 * Gets all design documents
	 *
	 * @return ViewResults - all design docs
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults getAllDesignDocuments() throws IOException {
		View v = new View(ALL_DOCS);
		v.startKey = "%22_design%2F%22";
		v.endKey = "%22_design0%22";
		v.includeDocs = Boolean.TRUE;
		return view(v, false);
	}

	/**
	 * Runs the standard "_all_docs" view on this database, with count
	 *
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults getAllDocumentsWithCount(int count) throws IOException {
		View v = new View(ALL_DOCS);
		v.setLimit(count); //v.setCount(count)

		return view(v, false);
	}

	/**
	 * Runs "_all_docs_by_update_seq?startkey=revision" view on this database
	 *
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults getAllDocuments(int revision) throws IOException {
		return view(new View("_all_docs_by_seq?startkey=" + revision), false);
	}

	/**
	 * Runs a named view on the database This will run a view and apply any
	 * filtering that is requested (reverse, startkey, etc).
	 *
	 * @param view
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults view(View view) throws IOException {
		return view(view, true);
	}

	public List<Object> getDocumentList(String viewName) throws IOException {
		Document doc= this.getAllDocuments();

		List<Object> dataList = new ArrayList<>();

		CouchResponseMapper<CfVmVO> couchResponse = new Gson().fromJson(
					doc.getJSONObject().toString(),
					new TypeToken<CouchResponseMapper<CfVmVO>>() {
				}.getType());

		for (CouchResponseRowMapper<CfVmVO> row : couchResponse.getRows()) {
			dataList.add((Object) row.getValue());
		}
		return dataList;
	}

	/**
	 * Runs a view, appending "_view" to the request if isPermanentView is true.
	 * *
	 *
	 * @param view
	 * @param isPermanentView
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private ViewResults view(final View view, final boolean isPermanentView) throws IOException {

		String url = null;

		if (isPermanentView) {
			String[] elements = view.getFullName().split("/");
			url = this.name
					+ "/"
					+ ((elements.length < 2) ? elements[0] : DESIGN
							+ elements[0] + VIEW + elements[1]);
		} else {
			url = this.name + "/" + view.getFullName();
		}

		CouchResponse resp;
		try {
			resp = session.get(url, view.getQueryString());
			if (resp.isOk()) {
				ViewResults results = new ViewResults(view, resp.getBodyAsJSONObject());
				results.setDatabase(this);
				return results;
			}

		} catch (UnsupportedEncodingException e) {
			log.error(e);
			throw e;
		}

		return null;

	}

	/**
	 * Runs a named view <i>Not currently working in CouchDB code</i>
	 *
	 * @param fullname
	 *            - the fullname (including the document name) ex:
	 *            foodoc:viewname
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public ViewResults view(String fullname) throws IOException {
		return view(new View(fullname), true);
	}

	/**
	 * Runs an ad-hoc view from a string
	 *
	 * @param function
	 *            - the Javascript function to use as the filter.
	 * @return results
	 */
	public ViewResults adhoc(String function) {
		return adhoc(new AdHocView(function));
	}

	/**
	 * Runs an ad-hoc view from an AdHocView object. You probably won't use this
	 * much, unless you want to add filtering to the view (reverse, startkey,
	 * etc...)
	 *
	 * @param view
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public ViewResults adhoc(final AdHocView view) {

		String adHocBody = new JSONStringer().object().key("map")
				.value(view.getFunction()).endObject().toString();

		// Bugfix - include query string for adhoc views to support
		// additional view options (setLimit, etc)
		CouchResponse resp;
		try {
			resp = session.post(name + "/_temp_view", adHocBody,
					view.getQueryString());

			if (resp.isOk()) {
				ViewResults results = new ViewResults(view,
						resp.getBodyAsJSONObject());
				results.setDatabase(this);
				return results;
			} else {
				log.warn("Error executing view - " + resp.getErrorId() + " "
						+ resp.getErrorReason());
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}

		return null;
	}

	/**
	 * Runs a view, appending "_view" to the request if isPermanentView is true.
	 *
	 * @return
	 */
	public JSONObject getStatus() {

		CouchResponse resp = session.get(this.name);
		if (resp.isOk()) {
			return resp.getBodyAsJSONObject();
		}

		return null;

	}

	/**
	 * Save a document at the given _id
	 * <p/>
	 * if the docId is null or empty, then this performs a POST to the database
	 * and retrieves a new _id.
	 * <p/>
	 * Otherwise, a PUT is called.
	 * <p/>
	 * Either way, a new _id and _rev are retrieved and updated in the Document
	 * object
	 *
	 * @param doc
	 * @param docId
	 */
	public void saveDocument(Document doc, String docId) throws IOException {
		CouchResponse resp;
		if (docId == null || docId.equals("")) {
			resp = session.post(name, doc.getJSONObject().toString());
		} else {
			resp = session.put(name + "/" + urlEncodePath(docId), doc
					.getJSONObject().toString());
		}

		if (resp.isOk()) {
			try {
				if (doc.getId() == null || doc.getId().equals("")) {
					doc.setId(resp.getBodyAsJSONObject().getString("id"));
				}
				doc.setRev(resp.getBodyAsJSONObject().getString("rev"));
			} catch (JSONException e) {
				log.error(e);
			}
			doc.setDatabase(this);
		} else {
			log.warn("Error adding document - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
			log.error(String.format("RESP: %s", resp));
			//System.err.println("RESP: " + resp)
		}
	}

	/**
	 * Save a document w/o specifying an id (can be null)
	 *
	 * @param doc
	 */
	public void saveDocument(Document doc) throws IOException {
		saveDocument(doc, doc.getId());
	}

	public void bulkSaveDocuments(Document[] documents) {
		CouchResponse resp = null;

		resp = session.post(name + "/_bulk_docs",
				new JSONObject().accumulate("docs", documents).toString());

		if (resp.isOk()) {
			// TODO set Ids and revs and name (db)
			final JSONArray respJsonArray = resp.getBodyAsJSONArray();
			JSONObject respObj = null;
			String id = null;
			String rev = null;
			for (int i = 0; i < documents.length; i++) {
				respObj = respJsonArray.getJSONObject(i);
				id = respObj.getString("id");
				rev = respObj.getString("rev");
				if (StringUtils.isBlank(documents[i].getId())) {
					documents[i].setId(id);
					documents[i].setRev(rev);
				} else if (StringUtils.isNotBlank(documents[i].getId())
						&& documents[i].getId().equals(id)) {
					documents[i].setRev(rev);
				} else {
					log.warn("returned bulk save array in incorrect order, saved documents do not have updated rev or ids");
				}
				documents[i].setDatabase(this);
			}
		} else {
			log.warn("Error bulk saving documents - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
		}
	}

	public void post(String data) {
		CouchResponse resp = null;
		resp = session.post(name, data);
		if (resp.isOk()) {
			// TODO set Ids and revs and name (db)

		} else {
			log.warn("Error bulk saving documents - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
		}
	}

	/**
	 * Retrieves a document from the CouchDB database
	 *
	 * @param id
	 * @return
	 */
	public Document getDocument(String id) {
		return getDocument(id, null, false);
	}

	/**
	 * Retrieves a document from the database and asks for a list of it's
	 * revisions. The list of revision keys can be retrieved from
	 * Document.getRevisions();
	 *
	 * @param id
	 * @return
	 */
	public Document getDocumentWithRevisions(String id) {
		return getDocument(id, null, true);
	}

	/**
	 * Retrieves a specific document revision
	 *
	 * @param id
	 * @param revision
	 * @return
	 */
	public Document getDocument(String id, String revision) {
		return getDocument(id, revision, false);
	}

	/**
	 * Retrieves a specific document revision and (optionally) asks for a list
	 * of all revisions
	 *
	 * @param id
	 * @param revision
	 * @param showRevisions
	 * @return the document
	 */
	public Document getDocument(String id, String revision,
			boolean showRevisions) {
		CouchResponse resp;
		Document doc = null;

		if (revision != null && showRevisions) {
			// resp = session.get(name + "/" + urlEncodePath(id), "rev=" + revision + "&full=true")
			resp = session.get(name + "/" + id, "rev=" + revision + "&full=true");
		} else if (revision != null) { // && !showRevisions
			// resp = session.get(name + "/" + urlEncodePath(id), "rev=" + revision)
			resp = session.get(name + "/" + id, "rev=" + revision);
		} else if (showRevisions) { // revision == null &&
			// resp = session.get(name + "/" + urlEncodePath(id), "revs=true")
			resp = session.get(name + "/" + id, "revs=true");
		} else {
			resp = session.get(name + "/" + id);
		}

		if (resp.isOk()) {
			doc = new Document(resp.getBodyAsJSONObject());
			doc.setDatabase(this);
		} else {
			log.warn("Error getting document - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
		}

		return doc;
	}

	/**
	 * Deletes a document
	 *
	 * @param d
	 * @return was the delete successful?
	 * @throws IllegalArgumentException
	 *             for blank document id
	 */
	public boolean deleteDocument(Document d) throws IOException {

		if (StringUtils.isBlank(d.getId())) {
			throw new IllegalArgumentException(
					"cannot delete document, doc id is empty");
		}

		CouchResponse resp = session.delete(name + "/"
				+ urlEncodePath(d.getId()) + "?rev=" + d.getRev());

		if (resp.isOk()) {
			return true;
		} else {
			log.warn("Error deleting document - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
			return false;
		}

	}

	/**
	 * Deletes a document
	 *
	 * @param d
	 * @return was the delete successful?
	 * @throws IllegalArgumentException
	 *             for blank document id
	 */
	public boolean deleteDocument(String id , String rev) throws IOException {

		CouchResponse resp = session.delete(name + "/"
				+ urlEncodePath(id) + "?rev=" + rev);

		if (resp.isOk()) {
			return true;
		} else {
			log.warn("Error deleting document - " + resp.getErrorId() + " "
					+ resp.getErrorReason());
			return false;
		}

	}

	/**
	 * Gets attachment
	 *
	 * @param id
	 * @param attachment
	 *            attachment body
	 * @return attachment body
	 */
	public String getAttachment(String id, String attachment)
			throws IOException {
		CouchResponse resp = session.get(name + "/" + urlEncodePath(id) + "/"
				+ attachment);
		return resp.getBody();
	}

	/**
	 * Puts attachment to the doc
	 *
	 * @param id
	 * @param fname
	 *            attachment name
	 * @param ctype
	 *            content type
	 * @param attachment
	 *            attachment body
	 * @return was the PUT successful?
	 */
	public String putAttachment(String id, String fname, String ctype,
			String attachment) throws IOException {
		CouchResponse resp = session.put(name + "/" + urlEncodePath(id) + "/"
				+ fname, ctype, attachment);
		return resp.getBody();
	}

	public void purge(JSONObject json) {
		CouchResponse resp = session.post(name + PURGE, json.toString());

		if (!resp.isOk()) {
			log.error("Error purge document- " + resp.getErrorId() + " "+ resp.getErrorReason());
		}
	}

	public void compact() {
		compact("");
	}

	public void compact(String viewName) {

		if (StringUtils.isEmpty(viewName)) {
			viewName = "";
		}
		CouchResponse resp = session.post(name+"/_compact/"+viewName,"");

		if (!resp.isOk()) {
			log.error(name+"/_compact/"+viewName);
			log.error("Error compact database - " + resp.getErrorId() + " "+ resp.getErrorReason());
		}
	}

}
