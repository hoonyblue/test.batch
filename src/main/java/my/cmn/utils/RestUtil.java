/**
 * copyright 2016 NCIS Cloud Portal System
 * @description
 * <pre></pre>
 *
 * @filename RestUtil.java
 *
 * @author bcpark
 * @lastmodifier bcpark
 * @created 2016. 10. 14.
 * @lastmodified 2016. 10. 14.

 *
 */
package my.cmn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;


public class RestUtil {

	private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);

	private Integer readTimeout;
	private Integer connectTimeout;
	private RestHeaders header;
	private MediaType mediaType;
	@SuppressWarnings("unused")
	private static final String UTF_8 = "UTF-8";


	public <V, T> ResponseEntity<T> send(String url, V sendObj, Class<T> responseType, HttpMethod method) {

		RestTemplate restTemplate = getRestTemplate();
		return restTemplate.exchange(url, method, getEntity(sendObj), responseType);
	}

	public <T> ResponseEntity<T> send(String url, Class<T> responseType, HttpMethod method) {
		RestTemplate restTemplate = getRestTemplate();
		return restTemplate.exchange(url, method, getEntity(), responseType);
	}


	private <T> HttpEntity<T> getEntity(T obj) {
		return new HttpEntity<>(obj, getHttpHeaders());
	}

	private HttpEntity<String> getEntity() {
		return getEntity("");
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        logger.debug("readTimeout : {} / connectTimeout : {}", getReadTimeout(), getConnectTimeout());

        factory.setReadTimeout(getReadTimeout());
        factory.setConnectTimeout(getConnectTimeout());
        return factory;
    }

	private RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				logger.error("Response error: {}, {}", response.getStatusCode(), response.getStatusText());
				HttpStatus statusCode = getHttpStatusCode2(response);
				switch (statusCode.series()) {
					case CLIENT_ERROR:
						throw new HttpClientErrorException(statusCode, response.getStatusText(),
								response.getHeaders(), getResponseBody2(response), Charsets.UTF_8);
					case SERVER_ERROR:
						throw new HttpServerErrorException(statusCode, response.getStatusText(),
								response.getHeaders(), getResponseBody2(response), Charsets.UTF_8);
					default:
						throw new RestClientException("Unknown status code [" + statusCode + "]");
				}
			}
			/**
			@Override
			protected boolean hasError(HttpStatus statusCode) {
				return super.hasError(statusCode);
			} */
			private HttpStatus getHttpStatusCode2(ClientHttpResponse response) throws IOException {
				HttpStatus statusCode;
				try {
					statusCode = response.getStatusCode();
				} catch (IllegalArgumentException ex) {
					throw new UnknownHttpStatusCodeException(response.getRawStatusCode(),
							response.getStatusText(), response.getHeaders(), getResponseBody2(response), Charsets.UTF_8);
				}
				return statusCode;
			}
			private byte[] getResponseBody2(ClientHttpResponse response) {
				try {
					InputStream responseBody = response.getBody();
					if (responseBody != null) {
						return FileCopyUtils.copyToByteArray(responseBody);
					}
				}
				catch (IOException ex) {
					// ignore
				}
				return new byte[0];
			}
		});
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charsets.UTF_8));
		/**
		for(HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
			logger.debug("getRestTemplate.messageConverter : {}", messageConverter.getClass());
		}
         */
		return restTemplate;
	}


	/**
	 * @return the readTimeout
	 */
	public Integer getReadTimeout() {
		/**
		if( ObjectUtils.isEmpty(readTimeout) )
			return Integer.valueOf(PropertiesUtil.getProperty("rest.readTimeout"));
		 */
		return readTimeout;
	}


	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}


	/**
	 * @return the connectTimeout
	 */
	public Integer getConnectTimeout() {
		/**
		if( ObjectUtils.isEmpty(connectTimeout) )
			return Integer.valueOf(PropertiesUtil.getProperty("rest.connectTimeout"));
		 */
		return connectTimeout;
	}


	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

    /**
     * @return the header
     */
    public RestHeaders getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(RestHeaders header) {
        this.header = header;
    }

    public void setContentType(MediaType mediaType) {
    	this.mediaType = mediaType;
    }
    public MediaType getContentType() {
    	return this.mediaType;
    }

    private HttpHeaders getHttpHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();

        // 2019-05-22 한글문제로 추가함.
        if (this.mediaType==null) {
        	httpHeaders.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        	logger.debug("contentType.charset: {}", httpHeaders.getContentType().getCharset());
        } else {
        	httpHeaders.setContentType(this.mediaType);
        }

        httpHeaders.add("AreaId", this.header.getAreaId());
        httpHeaders.add("Authorization", this.header.getAuthorization());

        if( !StringUtils.isEmpty(this.header.getZoneId()) )
            httpHeaders.add("ZoneId", this.header.getZoneId());

        if( !StringUtils.isEmpty(this.header.getNetworkId()) )
            httpHeaders.add("NetworkId", this.header.getNetworkId());

        if( !StringUtils.isEmpty(this.header.getSeq()) )
            httpHeaders.add("Seq", this.header.getSeq());

        if( !StringUtils.isEmpty(this.header.getAction()) )
            httpHeaders.add("Action", this.header.getAction());

        if( !StringUtils.isEmpty(this.header.getManagerId()) )
            httpHeaders.add("ManagerId", this.header.getManagerId());

        if( !StringUtils.isEmpty(this.header.getMachineIp()) )
            httpHeaders.add("MachineIp", this.header.getMachineIp());

        if( !StringUtils.isEmpty(this.header.getObjectname()) )
            httpHeaders.add("objectname", this.header.getObjectname());

        if( !StringUtils.isEmpty(this.header.getReason()) )
            httpHeaders.add("reason", this.header.getReason());

        if( !StringUtils.isEmpty(this.header.getTenant()) )
            httpHeaders.add("tenant", this.header.getTenant());

        return httpHeaders;
    }

}
