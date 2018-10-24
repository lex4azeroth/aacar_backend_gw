package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.PromotionModel;
import com.aawashcar.apigateway.model.PromotionWithServicesModel;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponseModel;

public interface EventPageService {

	List<PromotionModel> listEvents();
	
	List<PromotionWithServicesModel> listEventsWithServices();
	
	List<PromotionModel> listAvailableEvents(String validid);
	
	List<PromotionWithServicesModel> listAvailableEventsWithServices(String validid);
	
	WechatPayResponseModel purchasePromotion(PromotionModel promotion, String validId);
	
	void proccessWechatNotification(WechatNotify notify, int promotionTransactionId);
	
}
