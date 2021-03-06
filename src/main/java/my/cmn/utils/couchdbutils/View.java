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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * The View is the mechanism for performing Querys on a CouchDB instance.
 * The view can be named or ad-hoc (see AdHocView). (Currently [14 Sept 2007] named view aren't working in the
 * mainline CouchDB code... but this _should_ work.)
 *<p>
 * The View object exists mainly to apply filtering to the view.  Otherwise, views can be
 * called directly from the database object by using their names (or given an ad-hoc query).
 *
 * @author mbreese
 *
 */
public class View {
	private static final String DEFAULT_CHARSET = "UTF-8";
	protected String key;
	protected String startKey;
	protected String endKey;
	protected Integer limit;
	protected Boolean update;
	protected Boolean reverse;
	protected String skip;
    protected Boolean group;
	protected Boolean includeDocs;
	//protected String startkey
	//protected String endkey
	protected Integer groupLevel;

	protected String filter;
	protected String since;


	protected String name;
	protected Document document;
	protected String function;

	/**
	 * Build a view given a document and a name
	 *
	 * @param doc
	 * @param name
	 */
	public View(Document doc, String name) {
		this.document=doc;
		this.name=name;
	}

	/**
	 * Build a view given only a fullname ex: ("_add_docs", "_temp_view")
	 * @param fullname
	 */
	public View(String fullname) {
		this.name=fullname;
		this.document=null;
	}

	/**
	 * Builds a new view for a document, a given name, and the function definition.
	 * This <i>does not actually add it to the document</i>.  That is handled by
	 * Document.addView()
	 * <p>
	 * This constructor should only be called by Document.addView();
	 *
	 * @param doc
	 * @param name
	 * @param function
	 */
	View(Document doc, String name, String function) {
		this.name=name;
		this.document=doc;
		this.function=function;
	}

	/**
	 * Based upon settings, builds the queryString to add to the URL for this view.
	 *
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getQueryString() throws UnsupportedEncodingException {
		String queryString = "";
		if (key!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="key="+URLEncoder.encode(key,DEFAULT_CHARSET);
		}
		if (startKey!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="startkey="+URLEncoder.encode(startKey,DEFAULT_CHARSET);
		}
		if (endKey!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="endkey="+URLEncoder.encode(endKey,DEFAULT_CHARSET);
		}
		if (skip!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="skip="+URLEncoder.encode(skip,DEFAULT_CHARSET);
		}
		if (limit!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="limit="+limit;
		}
		if (update!=null && update.booleanValue()) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="update=true";
		}
		if (includeDocs!=null && includeDocs.booleanValue()) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="include_docs=true";
		}
		if (reverse!=null && reverse.booleanValue()) {
			if (!"".equals(queryString)) { queryString+="&"; }
		 	queryString+="descending=true";
		}
        if (group!=null && group.booleanValue()) {
        	if (!"".equals(queryString)) { queryString+="&"; }
        		queryString+="group=true";
        }
        if (groupLevel!=null) {
			if (!"".equals(queryString)) { queryString+="&"; }
			queryString+="group_level="+groupLevel;
		}
        if(filter!=null) {
	        	if (!"".equals(filter)) { queryString+="&"; }
	        	queryString+="filter="+filter;
        }
        if(since!=null) {
	        	if (!"".equals(since)) { queryString+="&"; }
	        	queryString+="since="+since;
        }

		return "".equals(queryString) ? null : queryString;

	}

	/**
	 * The number of entries to return
	 * @param count
         * @deprecated CouchDB 0.9 uses limit instead
	 */
	public void setCount(Integer count) {
		//this.count = count
		setLimit(count);
	}

    public void setKey(String key) {
      this.key = key;
    }

    public void setLimit(Integer limit) {
      this.limit = limit;
    }

    public void setGroup(Boolean group) {
      this.group = group;
    }

	/**
	 * Stop listing at this key
	 * @param endKey
	 */
	public void setEndKey(String endKey) {
		this.endKey = endKey;
	}
	/**
	 * Reverse the listing
	 * @param reverse
     * @deprecated CouchDB 0.9 uses "descending" instead
	 */
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

    public void setDescending(Boolean descending) {
        this.reverse = descending;
    }
	/**
	 * Skip listing these keys (not sure if this works, or the format)
	 * @param skip
	 */
	public void setSkip(String skip) {
		this.skip = skip;
	}
	/**
	 * Start listing at this key
	 * @param startKey
	 */
	public void setStartKey(String startKey) {
		this.startKey = startKey;
	}
	/**
	 * Not sure... might be for batch updates, but not sure.
	 * @param update
	 */
	public void setUpdate(Boolean update) {
		this.update = update;
	}

    public void setWithDocs(Boolean withDocs) {
		this.includeDocs = withDocs;
	}

	/**
	 * The name for this view (w/o doc id)
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * the full name for this view (w/ doc id, if avail)
	 * in the form of :
	 * "docid:name"
	 * or
	 * "name"
	 * @return
	 */
	public String getFullName() {
		return (document==null) ? name: document.getViewDocumentId()+"/"+name;
	}

	/**
	 * The function definition for this view, if it is available.
	 * @return
	 */
	public String getFunction() {
		return function;
	}

	/**
	public void setStartkey(String startkey) {
		this.startkey = startkey;
	}

	public void setEndkey(String endkey) {
		this.endkey = endkey;
	}
	 */


	public Integer getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(Integer groupLevel) {
		this.groupLevel = groupLevel;
	}

	/**
	public String getStartkey() {
		return startkey;
	}

	public String getEndkey() {
		return endkey;
	}
	 */

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	public String getSince()
	{
		return since;
	}

	public void setSince(String since)
	{
		this.since = since;
	}
}
