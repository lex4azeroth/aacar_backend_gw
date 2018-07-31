package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponseModel;

public interface EventPageService {

	List<PromotionModel> listEvents();
	
	List<PromotionModel> listAvailableEvents(String validid);
	
	WechatPayResponseModel purchasePromotion(PromotionModel promotion, String validId);
	
	void proccessWechatNotification(WechatNotify notify, int promotionTransactionId);
	
}
