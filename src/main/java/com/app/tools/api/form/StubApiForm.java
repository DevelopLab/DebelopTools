package com.app.tools.api.form;

public class StubApiForm {

	/** API名. */
	private String apiName;

	/** ポート. */
	private int port;

	/** URI. */
	private String uri;

	/** ボディ. */
	private String body;

	/** HTTPステータスコード. */
	private int httpStatusCode;

	/**
	 * @return apiName
	 */
	public String getApiName() {
		return this.apiName;
	}

	/**
	 * @param apiName set apiName
	 */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/**
	 * @return port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * @param port set port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return uri
	 */
	public String getUri() {
		return this.uri;
	}

	/**
	 * @param uri set uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
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
	 * @return httpStatusCode
	 */
	public int getHttpStatusCode() {
		return this.httpStatusCode;
	}

	/**
	 * @param httpStatusCode set httpStatusCode
	 */
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
}
