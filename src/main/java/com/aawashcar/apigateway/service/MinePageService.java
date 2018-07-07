package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;

public interface MinePageService {

	List<MyPromotionModel> listMyPromotionModels(String validId);
	
	List<MyCouponModel> listMyCouponModels(String validId);
}
