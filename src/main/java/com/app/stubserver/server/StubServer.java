package com.app.stubserver.server;

import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.app.stubserver.api.controller.StubController;
import com.app.tools.api.form.StubApiForm;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * スタブサーバ起動/停止クラス.
 *
 */
public class StubServer {

	/** ログ設定. */
	protected static Logger appLog = LoggerFactory.getLogger(StubServer.class);

	/** 開始制御. */
	public static AtomicBoolean isStarted = new AtomicBoolean(false);

	public static String showLog = "";

	/** HTTPサーバ.*/
	public HttpServer httpServer;

	/** HTTPアドレス. */
	private String httpBindIpAddr;

	/** HTTPポート. */
	private int httpBindPort;

	/** 処理スレッド. */
	private ExecutorService executor;

	/** 終了フラグ. */
	private static boolean quitMainFlag = false;

	private static final String LINE_SP = System.getProperty("line.separator");


	/**
	 * メインクラス.
	 */
	@Async
	public void main(StubApiForm stubApiForm) {
		try {
			setLog(String.format("スタブサーバの起動を開始します・・・"));

			// アドレスを生成
			httpBindIpAddr = "localhost";
			// ポート生成
			httpBindPort = stubApiForm.getPort();
			// スレッド生成(並列処理,指定数のスレッドプールを作成)
			executor = Executors.newFixedThreadPool(1);

			// サーバの初期設定
			init();

			// コンテキストの登録
			createContext(stubApiForm.getUri(), new StubController(stubApiForm.getApiName(), stubApiForm));

			// サーバの起動
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * HTTPサーバの初期設定を行う。
	 */
	public synchronized void init() {
		if (isStarted.get() == true) {
			setLog("ＨＴＴＰサービスは開始しています。");
			return;
		}

		synchronized (isStarted) {
			try {
				// Server生成 (HTTP)
				InetAddress addr = InetAddress.getByName(httpBindIpAddr);
				HttpServer server = HttpServer.create(new InetSocketAddress(addr, httpBindPort), 10);
				server.setExecutor(executor);
				httpServer = server;

			} catch (BindException e) {
				setErrorLog(String.format("ＨＴＴＰサービスのポートが既に使用中です。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort), e);
				System.exit(1);

			} catch (Exception e) {
				setErrorLog(String.format("ＨＴＴＰサービスのポートが既に使用中です。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort), e);
				System.exit(1);
			}
		}
	}

	/**
	 * コンテキストを登録する。
	 * @param uri uri
	 * @param httpHandler HttpHandler
	 */
	public synchronized void createContext(String uri, HttpHandler httpHandler) {
		setLog(String.format("サーバコンテキストを登録:URI=[%s]", uri));
		// コンテキストの登録
		httpServer.createContext(uri, httpHandler);
	}
	/**
	 * ＨＴＴＰサービスを開始する.
	 */
	public synchronized void start() {

		synchronized (isStarted) {
			try {
				// サービス開始
				httpServer.start();
				isStarted.set(true);
				setLog(String.format("ＨＴＴＰサービスを開始しました。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort));

			} catch (Exception e) {
				setErrorLog(String.format("ＨＴＴＰサービスのポートが既に使用中です。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort), e);
				System.exit(1);
			}
		}
		// 終了待ち
		quitMainFlag = false;
		while (!quitMainFlag) {
			try {
				wait();
			} catch (InterruptedException e) {
				setErrorLog("アクティビティー前のスレッドで割り込みが発生", e);
			}
		}

		// 終了時に少し待つ
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			setErrorLog("スレッドで割り込みが発生", e);
		}

		// サービス終了
		stop();
	}

	/**
	 * サービス終了
	 */
	public void stop() {
		if (isStarted.get() == false) {
			setLog(String.format("ＨＴＴＰサービスは開始していません。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort));
			return;
		}

		synchronized (isStarted) {
			if (httpServer != null) {
				httpServer.stop(1);
				httpServer = null;
			}
			if (executor != null) {
				executor.shutdown();
			}
			isStarted.set(false);
		}
		setLog(String.format("ＨＴＴＰサービスを「バルス！」しました。 ADDR=[%s] PORT=[%d]", httpBindIpAddr, httpBindPort));
	}

	/**
	 * メインスレッドの終了フラグを立てる。
	 */
	public synchronized void quitMain() {
		quitMainFlag = true;
		notifyAll();
	}

	public static void setLog(String log) {
		appLog.info(log);
		showLog += LINE_SP + log;
	}

	public static void setErrorLog(String log, Exception e) {
		appLog.error(log, e);
		showLog += LINE_SP + log;
	}

	public static synchronized String getLog() {
		return showLog;
	}

	public static synchronized void clearLog() {
		showLog = "";
	}
}
