package com.app.tools.api.form;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

public class CumberForm {

	private int requestCount;

	private String url;

	private String body;

	private String restType;

	private LinkedHashMap<String, String> headers;

	/**
	 * @return requestCount
	 */
	public int getRequestCount() {
		return this.requestCount;
	}

	/**
	 * @param requestCount set requestCount
	 */
	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url set url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return this.body;
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
		return this.restType;
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
	public LinkedHashMap<String, String> getHeaders() {
		return this.headers;
	}

	/**
	 * @param headers set headers
	 */
	public void setHeaders(String headers) {
		this.headers = new LinkedHashMap<String, String>();
		if (StringUtils.isEmpty(headers)) {
			return;
		}

		String[] headerArr = headers.split(",");
		for (String header : headerArr) {
			String [] mapArr = header.split(":");
			this.headers.put(mapArr[0], mapArr[1]);
		}
	}
}
