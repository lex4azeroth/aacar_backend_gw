package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.MyCouponModel;
import com.aawashcar.apigateway.model.MyPromotionModel;

public interface MinePageService {

	List<MyPromotionModel> listMyPromotionModels(String uuid);
	
	List<MyCouponModel> listMyCouponModels(String uuid);
}
