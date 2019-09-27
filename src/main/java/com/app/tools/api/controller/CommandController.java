package com.app.tools.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.stubserver.server.StubServer;
import com.app.tools.api.form.StubApiForm;
import com.app.tools.common.spring.AjaxMapping;

@Controller
@RequestMapping("/command")
public class CommandController {

	private static final StubServer stubServer = new StubServer();

	@RequestMapping("/edit")
	public String index() {
		return "command/edit";
	}

	@ResponseBody
	@AjaxMapping(value = "/start", produces="text/plain;charset=utf-8")
	public String serverStart(@RequestBody StubApiForm stubApiForm) {
		if (StubServer.isStarted.get()) {
			return  "{\"結果\":\"スタブサーバは、既に起動しています。．．．\"}";
		}

		stubServer.main(stubApiForm);
		return "{\"結果\":\"スタブサーバを停止しました。．．\"}";
	}

	@ResponseBody
	@AjaxMapping(value = "/stop", produces="text/plain;charset=utf-8")
	public String serverStop() {
		if (!StubServer.isStarted.get()) {
			return "{\"結果\":\"スタブサーバは、既に停止しています。．．．\"}";
		}
		stubServer.quitMain();

		return "{\"結果\":\"停止スタブサーバを要求中．．．\"}";
	}

	@ResponseBody
	@AjaxMapping(value = "/log", produces="text/plain;charset=utf-8")
	public String showLog() {
		return StubServer.getLog();
	}

	@ResponseBody
	@AjaxMapping(value = "/clearLog", produces="text/plain;charset=utf-8")
	public String clearLog() {
		StubServer.clearLog();
		return StubServer.getLog();
	}
}
