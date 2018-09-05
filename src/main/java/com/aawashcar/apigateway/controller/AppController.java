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

import com.aawashcar.apigateway.entity.MiniAuthEntity;

@RequestMapping("app/")
@ResponseBody
@Controller
public class AppController {
	@Value("${weixin.openid.url.get}")
	private String weixinUrl;

	@Value("${mcw.service.ops.url.prefix}")
	private String opsUrlPrefix;
	
	@Value("${mcw.service.crm.url.prefix}")
	private String crmUrlPrefix;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "getOpenId", method = RequestMethod.GET)
	public String getOpenId(@RequestParam("name") String name, @RequestParam("js_code") String jsCode,
			@RequestParam("grant_type") String grantType) {

		MiniAuthEntity miniAuth = getMiniAuth(name);

		String appId = miniAuth.getAppId();
		String secret = miniAuth.getSecret();

		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("appId", appId);
		urlVariables.put("secret", secret);
		urlVariables.put("jsCode", jsCode);
		urlVariables.put("grantType", grantType);

		ResponseEntity<String> response = restTemplate.exchange(weixinUrl, HttpMethod.GET, null, String.class,
				urlVariables);
		return response.getBody();
	}
	
	@RequestMapping(value = "testEurekaClients", method = RequestMethod.GET)
	public String testEurekaClients() {
		return restTemplate.getForEntity(crmUrlPrefix + "echo", String.class).getBody();
	}

	private MiniAuthEntity getMiniAuth(String appName) {
		String url = opsUrlPrefix + "miniauth/authpair/" + appName;
		MiniAuthEntity response = restTemplate.getForObject(url, MiniAuthEntity.class);
		return response;
	}
}
