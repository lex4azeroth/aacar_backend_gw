package com.aawashcar.apigateway.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {
	
	public static <T> ResponseEntity<T> exchange(RestTemplate restTemplate, String url, HttpMethod method,
	                                      Object requestBody, HttpHeaders headers, Class<T> responseType) {

		HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, headers);
//		log.debug("Request method: {}\nRequest header: {}\nRequest url: {} with body: {}", method,
//		          headers.toString(),
//		          finalURLString, requestBody);
		try {
//			log.debug("this.restTemplateWrapper.getRestTemplate() is {}", this.restTemplate);
//			log.debug("this.restTemplateWrapper.getRestTemplate().getInterceptors().size() is {}",
//			          restTemplate.getInterceptors().size());
			return restTemplate.exchange(url, method, requestEntity, responseType);
		}
		catch (Exception e) {
//			log.error("exchange error: {} {}", e.getClass(), e.getLocalizedMessage());
			throw e;
		}

	}

}
