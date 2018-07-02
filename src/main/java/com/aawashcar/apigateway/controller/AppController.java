package com.aawashcar.apigateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String getOpenId(@RequestParam("appid") String appId,
	                        @RequestParam("secret") String secret,
	                        @RequestParam("js_code") String jsCode,
	                        @RequestParam("grant_type") String grantType) {
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("appId", appId);
		urlVariables.put("secret", secret);
		urlVariables.put("jsCode", jsCode);
		urlVariables.put("grantType", grantType);

		ResponseEntity<String> response =
		                restTemplate.exchange(weixinUrl, HttpMethod.GET, null, String.class, urlVariables);
		return response.getBody();
	}
}
