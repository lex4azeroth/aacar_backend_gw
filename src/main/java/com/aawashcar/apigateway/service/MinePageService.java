package com.aawashcar.apigateway.service;

import java.util.List;
import java.util.Map;

import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;

public interface MinePageService {

	List<MyPromotionModel> listMyPromotionModels(String validId, Map<Integer, String> serviceNames);
	
	List<MyCouponModel> listMyCouponModels(String validId);
}
