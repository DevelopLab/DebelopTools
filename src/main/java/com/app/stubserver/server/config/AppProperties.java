package com.app.stubserver.server.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * アプリケーションプロパティ(app.properties)の値を保持するクラス.
 * 保持するタイミング --> サーバ起動時にプロパティの読み込みを行います。
 */
public class AppProperties {

	/** ログ設定. */
	protected Logger appLog = LoggerFactory.getLogger("app");

	/** API名達. */
	public String[] apiNames;

	/** アプリケーションMap. */
	public LinkedHashMap<String, StubApi> appMap;

	/**
	 * コンストラクタ.
	 */
	public AppProperties() {
		Properties prop = new Properties();
		try {
			// プロパティの読み込みを行う。
			prop.load(ClassLoader.getSystemResourceAsStream("app.properties"));
		} catch (IOException e) {
			appLog.error("app.propertiesの読み込みに失敗しました。", e);
			System.exit(1);
		}

		// 読み込みしたプロパティ値を変数に代入を行う。
		this.readAndSetParam(prop);
	}

	/**
	 * プロパティファイル読み込み⇒変数化.
	 */
	private void readAndSetParam(Properties prop) {
		// API名達を取得します。
		apiNames = getArraysStr(prop, "base.api.names");
		if (apiNames == null) {
			appLog.warn("読み込みに失敗したため、処理を終了します。。。");
			System.exit(1);
		}
		// API名の数だけ、Mapを生成
		appMap = new LinkedHashMap<String, StubApi>(apiNames.length);
		for (String apiName : apiNames) {
			// スタブAPI情報を生成
			StubApi stubApi = new StubApi();
			stubApi.uri = getStr(prop, "api.uri." + apiName);
			stubApi.body = getStr(prop, "api.body." + apiName);
			stubApi.httpStatusCode = getInt(prop, "api.http.status.code." + apiName);

			//　アプリケーションマップに追加。
			appMap.put(apiName, stubApi);
		}
	}

	/**
	 * プロパティファイルから文字列の値を取得する.
	 *
	 * @param prop プロパティ
	 * @param key キー
	 * @return プロパティ値
	 */
	private String getStr(Properties prop, String key) {
		String work = prop.getProperty(key);
		if (StringUtils.isEmpty(work)) {
			appLog.info(String.format("指定されたアプリケーションパラメータが取得できません。 [%s] <- [%s]", key, work));
			return null;
		}

		appLog.info(String.format("アプリケーションパラメータ設定 [%s] <- [%s]", key, work));
		return work;
	}

	/**
	 * プロパティファイルから数値（int型）の値を取得する
	 * ※ 数値として正しくない文字列の場合は、エラーメッセージを出力する
	 * @param prop
	 * @param key
	 * @return
	 */
	private int getInt(Properties prop, String key) {
		String work = prop.getProperty(key);
		if (StringUtils.isEmpty(work)) {
			appLog.warn(String.format("プロパティファイルの設定値が異状です。KEY=[%s],VALUE=[%s]", key, work));
			return 0;
		}

		try {
			int workInt = Integer.parseInt(work);
			appLog.info(String.format("システムパラメータ設定 [%s] <- [%d]", key, workInt));
			return workInt;
		} catch (NumberFormatException e) {
			appLog.warn(String.format("プロパティファイルの設定値が異状です。KEY=[%s],VALUE=[%s]", key, work), e);
			return 0;
		}
	}

	/**
	 * プロパティファイルから配列の文字列の値を取得する.
	 *
	 * @param prop プロパティ
	 * @param key キー
	 * @return プロパティ値
	 */
	private String[] getArraysStr(Properties prop, String key) {
		String work = prop.getProperty(key);
		if (StringUtils.isEmpty(work)) {
			appLog.info(String.format("指定されたアプリケーションパラメータが取得できません。 [%s] <- [%s]", key, work));
			return null;
		}

		appLog.info(String.format("アプリケーションパラメータ設定 [%s] <- [%s]", key, work));
		return work.split(",");
	}

	/**
	 * スタブAPIの各値を保持.
	 */
	public class StubApi {

		/** URI .*/
		public String uri;

		/** ボディ. */
		public String body;

		/** HTTPステータスコード .*/
		public int httpStatusCode;

		/**
		 * @return uri
		 */
		public String getUri() {
			return uri;
		}

		/**
		 * @param uri セットする uri
		 */
		public void setUri(String uri) {
			this.uri = uri;
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

		/**
		 * @return httpStatusCode
		 */
		public int getHttpStatusCode() {
			return httpStatusCode;
		}

		/**
		 * @param httpStatusCode セットする httpStatusCode
		 */
		public void setHttpStatusCode(int httpStatusCode) {
			this.httpStatusCode = httpStatusCode;
		}
	}
}
