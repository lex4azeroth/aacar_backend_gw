package com.aawashcar.apigateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class BaseService {
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Value("${mcw.service.oms.url.prefix}")
	protected String omsUrlPrefix;

	@Value("${mcw.service.crm.url.prefix}")
	protected String crmUrlPrefix;

	@Value("${mcw.service.ops.url.prefix}")
	protected String opsUrlPrefix;

	@Value("${mcw.service.prom.url.prefix}")
	protected String promUrlPrefix;
	
	@Value("${mcw.service.lbs.url.prefix}")
	protected String lbsUrlPrefix;
	
	@Value("${mcw.service.cap.url.prefix}")
	protected String capUrlPrefix;
}
