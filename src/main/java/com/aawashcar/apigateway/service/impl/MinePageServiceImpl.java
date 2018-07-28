package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;
import com.aawashcar.apigateway.service.MinePageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class MinePageServiceImpl extends BaseService implements MinePageService {

	@Override
	public List<MyPromotionModel> listMyPromotionModels(String validId) {
		String url = "%s/promotion/mylist/%s";
		int userId = getUserIdByOpenId(validId);
		if (userId == 0) {
			return null;
		}
		url = String.format(url, promUrlPrefix, userId);
		ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
		Promotion[] myPromotions = promotionResponseEntity.getBody();
		return EntityMapper.convertPromotionsToMyModel(myPromotions);
	}

	@Override
	public List<MyCouponModel> listMyCouponModels(String validId) {
		String url = "%s/coupon/mylist/%s";
		int userId = getUserIdByOpenId(validId);
		if (userId == 0) {
			return null;
		}
		url = String.format(url, promUrlPrefix, userId);
		ResponseEntity<Coupon[]> couponResponseEntity = restTemplate.getForEntity(url, Coupon[].class);
		Coupon[] myCoupons = couponResponseEntity.getBody();
		return EntityMapper.convertCouponsToMyModel(myCoupons);
	}
	
	private int getUserIdByOpenId(String openId) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, openId);
		User user = restTemplate.getForObject(url, User.class);
		int userId = user.getId();
		
		if (userId <= 0) {
			// log here
			return 0;
		} 
		
		return userId;
	}

}
