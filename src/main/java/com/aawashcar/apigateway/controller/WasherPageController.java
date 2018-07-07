package com.aawashcar.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.WasherMainPageInfo;
import com.aawashcar.apigateway.service.WasherPageService;

@RequestMapping("washer/")
@ResponseBody
@Controller
public class WasherPageController {

	@Autowired
	private WasherPageService service;
	
	///washer/main/login/{openid}
	@RequestMapping(value="main/login/{validid}", method = RequestMethod.GET)
	public WasherMainPageInfo login(@PathVariable("validid") String validId) {
		return service.login(validId);
	}
}
