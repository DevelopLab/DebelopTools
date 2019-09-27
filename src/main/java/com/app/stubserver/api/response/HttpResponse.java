package com.app.stubserver.api.response;

/**
 * HTTPレスポンスクラス.
 *
 */
public class HttpResponse {

	private int statusCode;

	private String body;

	/**
	 * @return statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode セットする statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body セットする body
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
