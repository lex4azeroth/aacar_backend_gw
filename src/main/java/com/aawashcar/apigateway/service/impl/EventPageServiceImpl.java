package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.service.EventPageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class EventPageServiceImpl implements EventPageService {
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${mcw.service.prom.url.prefix}")
	private String promUrlPrefix;
	
	@Override
	public List<PromotionModel> listEvents() {
		String url = promUrlPrefix + "promotion/listall";
		
		ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
		Promotion[] promotions = promotionResponseEntity.getBody();
		
		return EntityMapper.converPromotionToModel(promotions);
	}

}
