package com.aawashcar.apigateway.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@RequestMapping("app/")
@ResponseBody
@Controller
public class AppController {
	@Value("${weixin.openid.url.get}")
	private String weixinUrl;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "getOpenId", method = RequestMethod.GET)
	public String getOpenId(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String appId = MapUtils.getString(parameterMap, "appid");
		String secret = MapUtils.getString(parameterMap, "secret");
		String jsCode = MapUtils.getString(parameterMap, "js_code");
		String grantType = MapUtils.getString(parameterMap, "grant_type");
		String url = MessageFormat.format(weixinUrl, appId, secret, jsCode, grantType);
		return restTemplate.getForObject(url, String.class);
	}
}
