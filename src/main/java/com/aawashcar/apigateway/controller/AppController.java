package com.aawashcar.apigateway.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
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

import com.aawashcar.apigateway.entity.MiniAuthEntity;
import com.aawashcar.apigateway.util.WXPayUtil;
import com.aawashcar.apigateway.util.WeixinUtil;

//import net.sf.json.JSONObject;

//import net.sf.json.JSONObject;

@RequestMapping("app/")
@ResponseBody
@Controller
public class AppController {
	@Value("${weixin.openid.url.get}")
	private String weixinUrl;

	@Value("${mcw.service.ops.url.prefix}")
	private String opsUrlPrefix;

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "getOpenId", method = RequestMethod.GET)
	public String getOpenId(@RequestParam("name") String name,
	                        @RequestParam("js_code") String jsCode,
	                        @RequestParam("grant_type") String grantType) {
		
		MiniAuthEntity miniAuth = getMiniAuth(name);
		
		String appId = miniAuth.getAppId();
		String secret = miniAuth.getSecret();
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("appId", appId);
		urlVariables.put("secret", secret);
		urlVariables.put("jsCode", jsCode);
		urlVariables.put("grantType", grantType);

		ResponseEntity<String> response =
		                restTemplate.exchange(weixinUrl, HttpMethod.GET, null, String.class, urlVariables);
		return response.getBody();
	}

	private MiniAuthEntity getMiniAuth(String appName) {
		String url = opsUrlPrefix + "miniauth/authpair/" + appName;
		MiniAuthEntity response =
		                restTemplate.getForObject(url, MiniAuthEntity.class);
		return response;
	}
}
