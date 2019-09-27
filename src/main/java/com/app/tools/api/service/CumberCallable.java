package com.app.tools.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.app.stubserver.api.response.HttpResponse;
import com.app.tools.api.controller.CumberController;
import com.app.tools.api.form.CumberForm;

public class CumberCallable implements Callable<HttpResponse> {

	/** UserAPIサーバーコネクション. */
	private HttpURLConnection connection;

	/** VisaApiリクエスト情報. */
	private final CumberForm cumberForm;

	/** 実行回数. */
	public final int numberOfExecutions;

	public CumberCallable(CumberForm cumberForm, int numberOfExecutions) {
		this.cumberForm = cumberForm;
		this.numberOfExecutions = numberOfExecutions;
	}

	@Override
	public HttpResponse call() throws Exception {

		HttpResponse httpResponse = null;
		try {
			// URLを生成
			URL url = new URL(cumberForm.getUrl());

			connection = (HttpURLConnection) url.openConnection();
			CumberController.setLog(String.format("DebelopTools --> Api 実行回数=[%s] URL=[%s] BODY=[%s]", numberOfExecutions, url, cumberForm.getBody()));

			// ヘッダー情報を生成
			connection.setRequestMethod(cumberForm.getRestType());
			for (Entry<String, String> map : cumberForm.getHeaders().entrySet()) {
				connection.setRequestProperty(map.getKey(), map.getValue());
			}
			connection.setDoOutput(true);

			//リクエストボディ出力
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = cumberForm.getBody().getBytes("utf-8");
				os.write(input, 0, input.length);
				os.flush();
				os.close();
			}

			// タイムアウト設定
			connection.setConnectTimeout(3000);

			// HTTP接続
			connection.connect();

		} catch (Exception e) {
			CumberController.setErrorLog(String.format("接続に失敗しました。 実行回数=[%s] MSG=[%s]", numberOfExecutions, e.getMessage()), e);
		} finally {
			// HTTP接続の終了
			if (connection != null) {
				httpResponse = parseResponse();
				connection.disconnect();
			}
		}
		return httpResponse;
	}

	private HttpResponse parseResponse() {
		// レスポンス作成
		HttpResponse httpResponse = new HttpResponse();
		try {
			int statusCode = connection.getResponseCode();
			CumberController.setLog(String.format("DebelopTools <-- Api 実行回数=[%s] HTTP_STATUS=[%s]", numberOfExecutions, statusCode));
			// HTTP_STATUSが200以外の場合、ErrorStreamからBODY情報を取得
			if (connection.getErrorStream() != null) {
				httpResponse.setBody(responseToBody(connection.getErrorStream()));
			}
			else if (connection.getInputStream() != null) {
				httpResponse.setBody(responseToBody(connection.getInputStream()));
			}
			httpResponse.setStatusCode(statusCode);
		} catch (IOException e) {
			CumberController.setLog(String.format("ユーザAPIサーバの応答結果の取得に失敗しました。 実行回数=[%s] URL=[%s] MSG=[%s]",
					numberOfExecutions, connection.getURL(), e.getMessage()));
		}
		return httpResponse;
	}

	/**
	 * レスポンスBodyデータを取得する.
	 */
	private String responseToBody(InputStream inputStream) {
		final InputStreamReader inReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		final BufferedReader bufReader = new BufferedReader(inReader);
		StringBuilder builder = new StringBuilder();
		String line = null;
		// 1行ずつテキストを読み込む
		try {
			while ((line = bufReader.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}