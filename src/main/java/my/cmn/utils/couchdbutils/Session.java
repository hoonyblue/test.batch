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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The Session is the main connection to the CouchDB instance.  However, you'll only use the Session
 * to obtain a reference to a CouchDB Database.  All of the main work happens at the Database level.
 * <p>
 * It uses the Apache's  HttpClient library for all communication with the server.  This is
 * a little more robust than the standard URLConnection.
 * <p>
 * Ex usage: <br>
 * Session session = new Session(host,port);
 * Database db = session.getDatabase("dbname");
 *
 * @author mbreese
 * @author brennanjubb - HTTP-Auth username/pass
 */
@Component("couchDBSession")
public class Session {
	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final String MIME_TYPE_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-Type";

	protected Log log = LogFactory.getLog(Session.class);
	protected final String host;
	protected final int port;
	protected final String user;
	protected final String pass;
	protected final boolean secure;
	protected final boolean usesAuth;

	protected CouchResponse lastResponse;
	protected HttpClient httpClient;
	private HttpHost targetHost;

	//protected HttpParams httpParams

	/**
	 * Constructor for obtaining a Session with an HTTP-AUTH username/password and (optionally) a secure connection
	 * This isn't supported by CouchDB - you need a proxy in front to use this
	 * @param host - hostname
	 * @param port - port to use
	 * @param user - username
	 * @param pass - password
	 * @param secure  - use an SSL connection?
	 * @throws GeneralSecurityException
	 */
	public Session(String host, int port, String user, String pass, boolean usesAuth, boolean secure) throws GeneralSecurityException {

		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.usesAuth = usesAuth;
		this.secure = secure;

		String scheme = ( (secure) ? "https" : "http");
		this.targetHost = new HttpHost(this.host, this.port, scheme);

		try {

			HttpClientBuilder builder = HttpClientBuilder.create();

			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
						SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
						new String[] { "SSL" },
						null,
						new NoopHostnameVerifier()

					);


			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslConnectionFactory)
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.build();

			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
			connManager.setMaxTotal(100);
			connManager.setDefaultMaxPerRoute(5);

			builder.setConnectionManager(connManager);

			//Timeout 설정
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(15 * 1000)
					.setSocketTimeout(600 * 1000)
					.build();

			builder.setDefaultRequestConfig(requestConfig);
			builder.setUserAgent("couchdb4j");

			this.httpClient = builder.build();

		} catch (GeneralSecurityException e) {
			throw e;
		}
	}

	/**
	public Session(String host, int port, String user, String pass, boolean usesAuth, boolean secure) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.usesAuth = usesAuth;
		this.secure = secure;

        httpParams = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();

        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
		DefaultHttpClient defaultClient = new DefaultHttpClient(connManager, httpParams);
		if (user != null) {
			defaultClient.getCredentialsProvider().setCredentials( AuthScope.ANY, new UsernamePasswordCredentials(user, pass) );
		}

		this.httpClient = defaultClient;


		setUserAgent("couchdb4j");
		setSocketTimeout( (600 * 1000) );// M/R시 시간이 오래걸리므로 10분 셋팅
		setConnectionTimeout( (15 * 1000) );

	}
	 */

	/**
	 * Constructor for obtaining a Session with an HTTP-AUTH username/password
	 * This isn't supported by CouchDB - you need a proxy in front to use this
	 * @param host
	 * @param port
	 * @param user - username
	 * @param pass - password
	 * @throws GeneralSecurityException
	 */
	@Autowired
	public Session(@Value("${Couchdb.Host}") String host, @Value("${Couchdb.Port}") int port,
			@Value("${Couchdb.UserName}") String user, @Value("${Couchdb.Password}") String pass) throws GeneralSecurityException {
		this(host, port, user, pass, false, false);
	}

	/**
	 * Main constructor for obtaining a Session.
	 * @param host
	 * @param port
	 * @throws GeneralSecurityException
	 */
	public Session(String host, int port) throws GeneralSecurityException {
		this(host, port, null, null, false, false);
	}
	/**
	 * Optional constructor that indicates an HTTPS connection should be used.
	 * This isn't supported by CouchDB - you need a proxy in front to use this
	 *
	 * @param host
	 * @param port
	 * @param secure
	 * @throws GeneralSecurityException
	 */
	public Session(String host, int port, boolean secure) throws GeneralSecurityException {
		this(host, port, null, null, false, secure);
	}

	/**
	 * Read-only
	 * @return the host name
	 */
	public String getHost() {
		return host;
	}

	/**
	 * read-only
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Is this a secured connection (set in constructor)
	 * @return
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Retrieves a list of all database names from the server
	 * @return
	 */
	public List<String> getDatabaseNames() {
		CouchResponse resp = get("_all_dbs");
		org.json.JSONArray ar = resp.getBodyAsJSONArray();

		List<String> dbs = new ArrayList<>(ar.length());
		for (int i=0 ; i< ar.length(); i++) {
			dbs.add(ar.getString(i));
		}
		return dbs;
	}

	/**
	 * Loads a database instance from the server
	 * @param name
	 * @return the database (or null if it doesn't exist)
	 */
	public Database getDatabase(String name) {
		CouchResponse resp = get(name);
		if (resp.isOk()) {
			return new Database(resp.getBodyAsJSONObject(),this);
		} else {
			log.warn("Error getting database: "+name);
		}
		return null;
	}

	/**
	 * Creates a new database (if the name doesn't already exist)
	 * @param name
	 * @return the new database (or null if there was an error)
	 * @throws UnsupportedEncodingException
	 */
	public Database createDatabase(String name) {
		String dbname = name.toLowerCase().replaceAll("[^a-z0-9_$()+\\-/]", "_");
		if (!dbname.endsWith("/")) {
			dbname+="/";
		}
		CouchResponse resp = put(dbname);
		if (resp.isOk()) {
			return getDatabase(dbname);
		} else {
			log.warn("Error creating database: "+name);
			return null;
		}
	}
	/**
	 * Deletes a database (by name) from the CouchDB server.
	 * @param name
	 * @return true = successful, false = an error occurred (likely the database named didn't exist)
	 */
	public boolean deleteDatabase(String name) {
		return delete(name).isOk();
	}
	/**
	 * Deletes a database from the CouchDB server
	 * @param db
	 * @return was successful
	 */
	public boolean deleteDatabase(Database db) {
		return deleteDatabase(db.getName());
	}

	/**
	 * For a given url (such as /_all_dbs/), build the database connection url
	 * @param url
	 * @return the absolute URL (hostname/port/etc)
	 */
	protected String buildUrl(String url) {
		return buildUrl(url, "");
	}

	protected String buildUrl(String url, String queryString) {

		String tmpUrl = "/" + url;

		if( null != queryString && !"".equals(queryString) ) {
			tmpUrl += "?" + queryString;
		}

		return tmpUrl;
	}

	protected String buildUrl(String url, NameValuePair[] params) {

		StringBuilder tmpUrl = new StringBuilder();
		tmpUrl.append("/").append(url);

		if (params.length > 0) {
			tmpUrl.append("?");
		}

		for (NameValuePair param : params) {
			//tmpUrl += param.getName() + "=" + param.getValue()
			tmpUrl.append(param.getName()).append("=").append(param.getValue());
		}

		return tmpUrl.toString();

	}

	/**
	 * Package level access to send a DELETE request to the given URL
	 * @param url
	 * @return
	 */
	CouchResponse delete(String url) {
		HttpDelete del = new HttpDelete(buildUrl(url));
		return http(del);
	}

	/**
	 * Send a POST with no body / parameters
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	CouchResponse post(String url) {
		return post(url,null,null);
	}


	/**
	 * Send a POST with body
	 * @param url
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	CouchResponse post(String url, String content) {
		return post(url,content,null);
	}
	/**
	 * Send a POST with a body and query string
	 * @param url
	 * @param content
	 * @param queryString
	 * @return
	 */
	CouchResponse post(String url, String content, String queryString) {
		HttpPost post = new HttpPost(buildUrl(url, queryString));
		if (content!=null) {
			HttpEntity entity;
			entity = new StringEntity(content, DEFAULT_CHARSET);
			//entity = new StringEntity(content)
			post.setEntity(entity);
			post.setHeader(new BasicHeader(CONTENT_TYPE, MIME_TYPE_JSON));
		}


		return http(post);
	}

	/**
	 * Send a POST with a body, query string and specified content type
	 * @author rwilson
	 * @param url
	 * @param ctype
	 * @param content
	 * @param queryString
	 * @return
	 */
	CouchResponse post(String url, String ctype, String content, String queryString) {
	  HttpPost post = new HttpPost(buildUrl(url, queryString));
	  if (content!=null) {
	    HttpEntity entity;
			entity = new StringEntity(content, DEFAULT_CHARSET);
			//entity = new StringEntity(content)
				post.setEntity(entity);
				if (ctype != null) {
				  post.setHeader(new BasicHeader(CONTENT_TYPE, ctype));
				}
		}

		return http(post);
	}

	/**
	 * Send a PUT  (for creating databases)
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	CouchResponse put(String url) {
		return put(url,null);
	}
	/**
	 * Send a PUT with a body (for creating documents)
	 * @param url
	 * @param content
	 * @return
	 */
	CouchResponse put(String url, String content) {
		HttpPut put = new HttpPut(buildUrl(url));
		if (content!=null) {
			HttpEntity entity;
			entity = new StringEntity(content, DEFAULT_CHARSET);
			//entity = new StringEntity(content)
			put.setEntity(entity);
			put.setHeader(new BasicHeader(CONTENT_TYPE, MIME_TYPE_JSON));
		}
		return http(put);
	}

	/**
	 * Overloaded Put using by attachments
	 */
	CouchResponse put(String url, String ctype, String content) {
		HttpPut put = new HttpPut(buildUrl(url));
		if (content!=null) {
			HttpEntity entity;
			entity = new StringEntity(content, DEFAULT_CHARSET);
			//entity = new StringEntity(content)
			put.setEntity(entity);
			put.setHeader(new BasicHeader(CONTENT_TYPE, ctype));
		}
		return http(put);
	}

	/**
	 * Overloaded Put using by attachments and query string
	 * @author rwilson
	 * @param url
	 * @param ctype
	 * @param content
	 * @param queryString
	 * @return
	 */
	CouchResponse put(String url, String ctype, String content, String queryString) {
		HttpPut put = new HttpPut(buildUrl(url, queryString));
		if (content!=null) {
			HttpEntity entity;
			entity = new StringEntity(content, DEFAULT_CHARSET);
			//entity = new StringEntity(content)
			put.setEntity(entity);
			if (ctype!=null) {
				put.setHeader(new BasicHeader(CONTENT_TYPE, ctype));
			}
		}
		return http(put);
	}

	/**
	 * Send a GET request
	 * @param url
	 * @return
	 */
	public CouchResponse get(String url) {
		HttpGet get = new HttpGet(buildUrl(url));
		return http(get);
	}
	/**
	 * Send a GET request with a number of name/value pairs as a query string
	 * @param url
	 * @param queryParams
	 * @return
	 */
	CouchResponse get(String url, NameValuePair[] queryParams) {
		HttpGet get = new HttpGet(buildUrl(url, queryParams));
		return http(get);
	}

	/**
	 * Send a GET request with a queryString (?foo=bar)
	 * @param url
	 * @param queryString
	 * @return
	 */
	CouchResponse get(String url, String queryString) {
		HttpGet get = new HttpGet(buildUrl(url, queryString));
		return http(get);
	}

	/**
	 * Method that actually performs the GET/PUT/POST/DELETE calls.
	 * Executes the given HttpMethod on the HttpClient object (one HttpClient per Session).
	 * <p>
	 * This returns a CouchResponse, which can be used to get the status of the call (isOk),
	 * and any headers / body that was sent back.
	 *
	 * @param req
	 * @return the CouchResponse (status / error / json document)
	 */
	protected CouchResponse http(HttpRequestBase req) {

		HttpResponse httpResponse = null;
		HttpEntity entity = null;

		try {
			if (usesAuth) {
				//req.getParams().setBooleanParameter(ClientPNames.HANDLE_AUTHENTICATION, true); // NOSONAR
				RequestConfig sc = RequestConfig.copy(req.getConfig()).setAuthenticationEnabled(true).build();
				req.setConfig(sc);
			}
			//req.getParams().setParameter(AllClientPNames.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET)

			if( null != this.user && !"".equals(this.user) ) {

				CredentialsProvider provider = new BasicCredentialsProvider();
				provider.setCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(this.user, this.pass)
					);

				AuthCache authCache = new BasicAuthCache();
				authCache.put(targetHost, new BasicScheme());

				HttpClientContext context = HttpClientContext.create();
				context.setCredentialsProvider(provider);
				context.setAuthCache(authCache);

				httpResponse = httpClient.execute(targetHost, req, context);

			} else {

				httpResponse = httpClient.execute(targetHost, req);

			}

			entity = httpResponse.getEntity();

			lastResponse = new CouchResponse(req, httpResponse);



		} catch (IOException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		} finally {
			  if (entity != null) {
				try {
					//entity.consumeContent(); // NOSONAR
					EntityUtils.consume(entity);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			  }
		}
		return lastResponse;
	}

	/**
	 * Returns the last response for this given session
	 * - useful for debugging purposes
	 * @return
	 */
	public CouchResponse getLastResponse() {
		return lastResponse;
	}

	protected String encodeParameter(String paramValue) {
		try
		{
			return URLEncoder.encode(paramValue, DEFAULT_CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

}