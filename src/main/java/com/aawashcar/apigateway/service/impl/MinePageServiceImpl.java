package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;
import com.aawashcar.apigateway.service.MinePageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class MinePageServiceImpl implements MinePageService {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${mcw.service.prom.url.prefix}")
	private String promUrlPrefix;
	
	@Value("${mcw.service.crm.url.prefix}")
	private String crmUrlPrefix;

	@Override
	public List<MyPromotionModel> listMyPromotionModels(String uuid) {
		String url = "%s/promotion/mylist/%s";
		url = String.format(url, promUrlPrefix, getUserIdByUuid(uuid));
		ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
		Promotion[] myPromotions = promotionResponseEntity.getBody();
		return EntityMapper.convertPromotionsToMyModel(myPromotions);
	}

	@Override
	public List<MyCouponModel> listMyCouponModels(String uuid) {
		String url = "%s/coupon/mylist/%s";
		url = String.format(url, promUrlPrefix, getUserIdByUuid(uuid));
		ResponseEntity<Coupon[]> couponResponseEntity = restTemplate.getForEntity(url, Coupon[].class);
		Coupon[] myCoupons = couponResponseEntity.getBody();
		return EntityMapper.convertCouponsToMyModel(myCoupons);
	}
	
	private int getUserIdByUuid(String uuid) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, uuid);
		User user = restTemplate.getForObject(url, User.class);
		int userId = user.getId();
		
		if (userId <= 0) {
			throw new RuntimeException();
		} 
		
		return userId;
	}

}
