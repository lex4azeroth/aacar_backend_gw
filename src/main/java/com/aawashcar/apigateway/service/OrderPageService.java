package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;
import com.aawashcar.apigateway.model.WechatNotify;
import com.aawashcar.apigateway.model.WechatPayResponseModel;

public interface OrderPageService {

	List<OrderSummaryModel> myOrderSummaryList(String id, int size);
	
	OrderDetailModel myOrderDetail(int orderId, String validId);
	
	OrderDetailWithWasherModel orderDetailWithWasher(int orderId);
	
	List<OrderDetailWithWasherModel> listAllOrderDetails();
	
	Pricing pricing(String validId, int orderId, int couponId, int promotionId);
	
	WechatPayResponseModel pay(Pricing pricing);
	
	void proccessWechatNotification(WechatNotify notify, int orderId);
}
