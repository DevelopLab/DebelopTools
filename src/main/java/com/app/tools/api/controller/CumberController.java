package com.app.tools.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.stubserver.api.response.HttpResponse;
import com.app.tools.api.form.CumberForm;
import com.app.tools.api.service.CumberCallable;
import com.app.tools.common.spring.AjaxMapping;

@Controller
@RequestMapping("/cumber")
public class CumberController {

	private static boolean isStarted;

	/** ログ設定. */
	protected static Logger logger = LoggerFactory.getLogger(CumberCallable.class);

	private static String showLog = "";

	private static final String LINE_SP = System.getProperty("line.separator");

	@RequestMapping("/edit")
	public String index() {
		return "cumber/edit";
	}

	@ResponseBody
	@AjaxMapping(value = "/start", produces="text/plain;charset=utf-8")
	public String start(@RequestBody CumberForm cumberForm) {
		if (isStarted) {
			return  "{\"結果\":\"負荷ツールは、既に起動しています。．．．\"}";
		}
		isStarted = true;
		try {
			List<Future<HttpResponse>> futures = new ArrayList<>();
			ExecutorService threadPool = Executors.newFixedThreadPool(cumberForm.getRequestCount());
			int numberOfExecutions = 0;
			for (int i = 0; i < cumberForm.getRequestCount(); i++) {
				numberOfExecutions++;
				// 非同期実行
	    		Future<HttpResponse> future = threadPool.submit(new CumberCallable(cumberForm, numberOfExecutions));
	    		futures.add(future);
			}

        	// 完了するまで待機し実行計算結果を取得する。
			int resuleCount = 0;
	        for (Future<HttpResponse> future : futures) {
	        	try {
	        		resuleCount++;
	        		future.get(3000, TimeUnit.MILLISECONDS);
	        	} catch (Exception e) {
	        		setErrorLog(String.format("応答結果が受信できませんでした。 実行回数=[%s]", resuleCount), e);
				}
	        }
		} catch (Exception e) {
			logger.warn("APIへのリクエスト送信に失敗しました。", e);
			isStarted = false;
		}
		isStarted = false;
		return "{\"結果\":\"負荷ツール処理が完了しました。．．．\"}";
	}

	@ResponseBody
	@AjaxMapping(value = "/log", produces="text/plain;charset=utf-8")
	public String showLog() {
		return getLog();
	}

	@ResponseBody
	@AjaxMapping(value = "/clearLog", produces="text/plain;charset=utf-8")
	public String clearLog() {
		showLog = "";
		return getLog();
	}

	public static void setLog(String log) {
		logger.info(log);
		showLog += LINE_SP + log;
	}

	public static void setErrorLog(String log, Exception e) {
		logger.error(log, e);
		showLog += LINE_SP + log;
	}

	public static synchronized String getLog() {
		return showLog;
	}
}
