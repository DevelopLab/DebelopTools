package com.app.stubserver.api.controller;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import com.app.stubserver.server.StubServer;
import com.app.tools.api.form.StubApiForm;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * スタブコントローラー.
 *
 */
public class StubController implements HttpHandler {

	private final String apiName;

	private final StubApiForm stubApi;

	/**
	 * コンストラクタ.
	 *
	 * @param apiName API名
	 * @param stubApi API情報
	 */
	public StubController(String apiName, StubApiForm stubApi) {
		this.apiName = apiName;
		this.stubApi = stubApi;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		String path = requestUriPath(exchange);
		try {
			StubServer.setLog(String.format("API=[%s] URI=[%s] リクエストを受信", apiName, exchange.getRequestURI()));

			// レスポンス情報を設定
			OutputStream out = exchange.getResponseBody();

			// HTTP-STATUS-CODEを設定
			exchange.sendResponseHeaders(stubApi.getHttpStatusCode(), 0);
			// BODYを設定
			if (StringUtils.isNotEmpty(stubApi.getBody())) {
				out.write(stubApi.getBody().getBytes());
			}
		} finally {
			exchange.close();
			StubServer.setLog(String.format("API=[%s] URI=[%s] レスポンスを送信", apiName, exchange.getRequestURI() ));
		}
	}

	/**
	 * 受信したリクエストURIパスを取得する.
	 *
	 * @param exchange
	 * @return URIパス
	 */
	public String requestUriPath(HttpExchange exchange) {
		String path0 = exchange.getHttpContext().getPath();
		String path1 = exchange.getRequestURI().getPath();
		return path1.substring(path0.length());
	}
}
