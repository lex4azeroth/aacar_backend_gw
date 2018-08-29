package com.aawashcar.apigateway.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
	@Value("${maxConnPerRoute:2000}")
	private int maxConnPerRoute;
	@Value("${maxConnTotal:4000}")
	private int maxConnTotal;

	@Bean
	@Primary
	@LoadBalanced
	public RestTemplate restTemplate() {
		ClientHttpRequestFactory fac = new HttpComponentsClientHttpRequestFactory(
		                                                                          getHttpClient());
		RestTemplate ret = new RestTemplate(fac);
		return ret;
	}

	@Bean
	@Primary
	public HttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom()
		                                            .setMaxConnPerRoute(maxConnPerRoute)
		                                            .setMaxConnTotal(maxConnTotal)
		                                            .disableCookieManagement()
		                                            .build();
		return httpClient;
	}
	
	
}
