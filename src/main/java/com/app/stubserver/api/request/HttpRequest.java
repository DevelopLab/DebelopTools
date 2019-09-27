package com.app.stubserver.api.request;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.net.httpserver.Headers;

/**
 * HTTPリクエスト情報.
 *
 */
public class HttpRequest {

	private String uri;

	private LinkedHashMap<String, String> parameters;

	private String body;

	private String restType;

	private LinkedHashMap<String, String> headers;

	/**
	 * @return uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri set uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters set parameters
	 */
	public void setParameters(LinkedHashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body set body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return restType
	 */
	public String getRestType() {
		return restType;
	}

	/**
	 * @param restType set restType
	 */
	public void setRestType(String restType) {
		this.restType = restType;
	}

	/**
	 * @return headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers set headers
	 */
	public void setHeaders(Headers headers) {
		LinkedHashMap<String, String> headersMap = new LinkedHashMap<String, String>();
		if (headers == null) {
			this.headers = headersMap;
			return;
		}
		// リクエストヘッダー情報をMapに格納
		for (Entry<String, List<String>> map : headers.entrySet()) {
			// キー「1」に対して、値は複数存在する
			for (String value : map.getValue()) {
				headersMap.put(map.getKey(), value);
			}
		}
		this.headers = headersMap;
	}
}
