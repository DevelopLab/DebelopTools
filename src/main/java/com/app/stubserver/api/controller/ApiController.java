package com.app.stubserver.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.stubserver.api.request.HttpRequest;
import com.app.stubserver.api.response.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * API制御の基底クラス.
 *
 */
public abstract class ApiController implements HttpHandler {

	/** ログ設定. */
	protected final Logger appLog = LoggerFactory.getLogger("app");

	protected final String apiName;

	/**
	 * コンストラクタ.
	 *
	 * @param apiName API名
	 */
	public ApiController(String apiName) {
		this.apiName = apiName;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String uri = exchange.getHttpContext().getPath();
		appLog.info(String.format("API=[%s] URI=[%s] リクエストを受信", apiName, uri));

		try {
			String path = requestUriPath(exchange);
			LinkedHashMap<String, String> parameters = this.queryToMap(path);
			String body = requestToBody(exchange);
			appLog.info(String.format("API=[%s] QUERY=[%s] BODY=[%s]", apiName, parameters, body));

			// リクエスト情報を生成
			HttpRequest httpRequest = new HttpRequest();
			// URIを設定
			httpRequest.setUri(path);
			// URLパラメータを設定
			httpRequest.setParameters(parameters);
			// ヘッダーを設定。
			httpRequest.setHeaders(exchange.getRequestHeaders());
			// RESTを設定
			httpRequest.setRestType(exchange.getRequestMethod());
			// BODYを設定
			httpRequest.setBody(body);

			// 派生クラスにより処理
			HttpResponse response = this.controller(httpRequest);

			// レスポンス情報を設定
			OutputStream out = exchange.getResponseBody();
			// HTTP-STATUS-CODEを設定
			exchange.sendResponseHeaders(response.getStatusCode(), 0);
			// BODYを設定
			if (StringUtils.isNotEmpty(response.getBody())) {
				out.write(response.getBody().getBytes());
			}
		} catch (Exception e) {
			appLog.error(String.format("補足されない例外が発生しました。 API=[%s] URI=[%s]", apiName, uri), e);
		} finally {
			exchange.close();
			appLog.info(String.format("API=[%s] URI=[%s] レスポンスを送信", apiName, uri));
		}
	}

	/**
	 * 派生クラスに処理の制御をさせる.
	 *
	 * @param httpRequest リクエスト情報
	 * @return レスポンス情報
	 */
	public abstract HttpResponse controller(HttpRequest httpRequest);

	/**
	 * 受信したリクエストURIパスを取得する.
	 *
	 * @param exchange HttpExchange
	 * @return URIパス
	 */
	private String requestUriPath(HttpExchange exchange) {
		String path0 = exchange.getHttpContext().getPath();
		String path1 = exchange.getRequestURI().getPath();
		return path1.substring(path0.length());
	}

	/**
	 * URLパラメータのパースを行う。
	 *
	 * @param query URLパラメータ
	 * @return パラメータ
	 */
	private LinkedHashMap<String, String> queryToMap(String query) {
		final LinkedHashMap<String, String> queryMap = new LinkedHashMap<String, String>();
		if (StringUtils.isBlank(query)) {
			return queryMap;
		}
		final String[] pairs = query.split("&");

        try {
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                queryMap.put(key, value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        return queryMap;
	}

	/**
	 * リクエストBodyデータを取得する.
	 *
	 * @param exchange HttpExchange
	 * @return リクエストBody
	 */
	private String requestToBody(HttpExchange exchange) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
		StringBuilder builder = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return builder.toString();
	}

	/**
	 * JSON文字列を戻りのClassに合わせて変換する.
	 *
	 * @param object 変換するクラス
	 * @param json 変換をかけるjson文字列
	 * @return json
	 */
	protected <T> T parseToClass(Class<T> object, String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return (T) mapper.readValue(json, object);
		} catch (IOException e) {
			appLog.error(String.format("[Json -> class]への変換に失敗しました。 API=[%s] JSON=[%s]", apiName, json) ,e);
			return null;
		}
	}

	/**
	 * JavaオブジェクトをJSON文字列に変換する。
	 *
	 * @param object 変換クラス
	 * @return Json文字列
	 */
	protected String convertToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			appLog.error(String.format("[class --> json]への変換に失敗しました。 API=[%s] CLASS=[%s]", apiName, object) ,e);
			return null;
		}
	}
}
