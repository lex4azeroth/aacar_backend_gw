package com.aawashcar.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.service.MainPageInfoService;

@RequestMapping("main/")
@ResponseBody
@Controller
public class MainPageController {

	@Autowired
	private MainPageInfoService service;

	@RequestMapping(value = "pageinfo/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public MainPageInfo mainPageInfo(@PathVariable("uuid") String uuid) {
		return service.getMainPageInfo(uuid);
	}
}
