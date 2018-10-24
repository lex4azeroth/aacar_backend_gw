package com.aawashcar.apigateway.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Autowired
	private HttpClient httpClient;

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

//		ResponseEntity<String> response = restTemplate.exchange(weixinUrl, HttpMethod.GET, null, String.class,
//				urlVariables);
		
		// weixin.openid.url.get=https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={secret}&js_code={jsCode}&grant_type={grantType}
		weixinUrl = weixinUrl.replace("appIdValue", appId);
		weixinUrl = weixinUrl.replace("secretValue", secret);
		weixinUrl = weixinUrl.replace("jsCodeValue", jsCode);
		weixinUrl = weixinUrl.replace("grantTypeValue", grantType);
		System.out.println(weixinUrl);
		HttpGet get = new HttpGet(weixinUrl);
		HttpResponse response;
		String result = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity,"UTF-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
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
