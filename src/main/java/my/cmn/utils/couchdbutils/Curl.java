package my.cmn.utils.couchdbutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

/**
 * This class takes on the simple task of doing http calls to any http web service like a web page or alike. Since the
 * class is streamlined for JSON use this is the most easiest to set up.
 *
 * @author mikael.p.larsson@epsilon.nu
 *
 */
public class Curl {

    private Map<String, String> requestProperties = null;
    private String charsetName = "UTF8";

    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String POST = "POST";
    public static final String HEAD = "HEAD";
    public static final String DELETE = "DELETE";

    /**
     * This is the default constructor for Curl which takes it for granted that you want to communicate and read JSON.
     * Most of the times this approach works even if plain html or text is requested.
     */
    public Curl() {
        requestProperties = new HashMap<>();
        requestProperties.put("Content-Type", "application/json");
    }

    /**
     * With this alternate constructor a map containing header strings can be provided, useful if something apart from
     * JSON is to be consumed.
     *
     * @param requestProperties
     *            a Map containing the header strings.
     */
    public Curl(Map<String, String> requestProperties) {
        this.requestProperties = requestProperties;
    }

    /**
     * Public setter to enable setting charsetName.
     *
     * @param charsetName
     * @return this instance to enable on liners.
     */
    public Curl setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    /**
     * In the world of the web this is the command that a web browser does for you after you have entered an url into
     * the address field. When using GET there should be no side effects on the site the data was requested from; the
     * get method only consumes data and sends nothing.
     *
     * @param urlAsString
     *            the url to fetch from the internet.
     * @return The response from the server
     */
    public String get(String urlAsString) {
        return doHttpCall(urlAsString, GET, null);
    }

    /**
     * Put should be used when a resource should be sent to a server.
     *
     * @param urlAsString
     *            the url to the resource.
     * @param doc
     *            the content to put
     * @return The response from the server.
     */
    public String put(String urlAsString, String doc) {
        return doHttpCall(urlAsString, "PUT", doc);
    }

    /**
     * Post should be used when a resource should be posted to a server.
     *
     * @param urlAsString
     *            the url to the resource.
     * @param doc
     *            the content to put
     * @return The response from the server.
     */
    public String post(String urlAsString, String doc) {
        return doHttpCall(urlAsString, "POST", doc);
    }

    /**
     * Mostly to be considered as a get without the contents, Here implemented as an is the resource available function.
     *
     * @param urlAsString
     *            the url to the resource.
     * @return The responseMessage from the server.
     */
    public String head(String urlAsString) {
        return doHttpCall(urlAsString, "HEAD", null);
    }

    /**
     * Deletes a resource from an url. Be careful!
     *
     * @param urlAsString
     *            The url to the resource to delete.
     * @return The response from the server.
     */
    public String delete(String urlAsString) {
        try {
            return doHttpCall(urlAsString, "DELETE", null);
        } catch (Exception e) {
  //          Log.warn("No object to delete found at " + urlAsString + ".")
            return "No object to delete found at " + urlAsString + ".";
        }
    }

    /**
     * This method does the actual HTTP communication to simplify the methods above.
     *
     * @param urlAsString
     *            The url to resource in question.
     * @param method
     *            The method to be used.
     * @param doc
     *            The resource to send or null if none.
     * @return The response from the server.
     */
    private String doHttpCall(String urlAsString, String method, String doc) {
        StringBuilder result = new StringBuilder(); //StringBuffer result = new StringBuffer()
        HttpURLConnection httpUrlConnection = null;

        try {
            URL url = new URL(urlAsString);
            httpUrlConnection = (HttpURLConnection) url.openConnection();


            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod(method);

            if (url.getUserInfo() != null) {
                String basicAuth = "Basic " + new String(new Base64().encode(url.getUserInfo().getBytes()));
                httpUrlConnection.setRequestProperty("Authorization", basicAuth);
            }
            httpUrlConnection.setRequestProperty("Content-Length", "0");
            /**
            for (String key : requestProperties.keySet()) {
              httpUrlConnection.setRequestProperty(key, requestProperties.get(key));
        		} */
            for (Map.Entry<String,String> entry : requestProperties.entrySet()) {
                httpUrlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (doc != null && !doc.isEmpty()) {
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setRequestProperty("Content-Length", "" + doc.getBytes(charsetName));

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpUrlConnection.getOutputStream(),
                        charsetName);
                outputStreamWriter.write(doc);
                outputStreamWriter.close();

            }

            readInputStream(result, httpUrlConnection.getInputStream());

        } catch (RuntimeException e) {
//            Log.info(e.getMessage())
        } catch (MalformedURLException e) {
//            Log.warn("The url '" + urlAsString + "' is malformed.")
        } catch (IOException e) {
            try {
                result.append(e.getMessage());
                readInputStream(result, httpUrlConnection.getErrorStream());
                if ("".equals(result.toString())) {
                    result.append("Error ");
                    result.append(httpUrlConnection.getResponseCode());
                    result.append(" : ");
                    result.append(httpUrlConnection.getResponseMessage());
                    result.append(".  Exception message is: [");
                    result.append(e.getMessage());
                    result.append("]");
                }
            } catch (IOException e1) {
            }
        } finally {
            if ("HEAD".equalsIgnoreCase(method)) {
                try {
                    result.append(httpUrlConnection.getResponseMessage());
                } catch (IOException e) {
 //                   Log.fatal("This is as low as we can get, nothing worked!")
                    e.printStackTrace();
                }
            }
            if (httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }
        return result.toString();
    }

    /**
     * Local helper method that reads data from an inputstream.
     *
     * @param result
     *            The read text.
     * @param inputStream
     *            The stream to read.
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private void readInputStream(StringBuilder result, InputStream inputStream) throws UnsupportedEncodingException,
            IOException {
        if (inputStream == null)
            throw new IOException("No working inputStream.");
        InputStreamReader streamReader = new InputStreamReader(inputStream, charsetName);
        BufferedReader bufferedReader = new BufferedReader(streamReader);

        String row;
        while ((row = bufferedReader.readLine()) != null) {
            result.append(row);
            result.append("\n");
        }

        bufferedReader.close();
        streamReader.close();
    }

    /**
     * A main method to provide the possibility to use this exact class from the command line.
     * <p>
     * usage:
     * <code>java -cp target/classes/. path.to.the.class.Curl http://server.domain.nu:port/path/to/resource method [data]</code>
     * </p>
     *
     * @param args
     *            in order: url method data
     */
    /**
     public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("usage: Curl path method [data]");
            System.exit(0);
        }
        String url = args[0];
        String method = args[1];
        String data = args.length == 3 ? args[2] : null;
        Curl curl = new Curl();

        if (method.equals("head")) {
            System.out.println(curl.head(url));
            System.exit(0);
        }
        if (method.equals("put")) {
            System.out.println(curl.put(url, data));
            System.exit(0);
        }

        System.out.println(curl.doHttpCall(url, method, data));
    }*/

}
