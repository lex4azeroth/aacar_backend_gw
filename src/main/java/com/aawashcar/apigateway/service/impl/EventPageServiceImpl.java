package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.service.EventPageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class EventPageServiceImpl extends BaseService implements EventPageService {

	@Override
	public List<PromotionModel> listEvents() {
		String url = promUrlPrefix + "promotion/listall";

		ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
		Promotion[] promotions = promotionResponseEntity.getBody();

		return EntityMapper.converPromotionToModel(promotions);
	}
}
