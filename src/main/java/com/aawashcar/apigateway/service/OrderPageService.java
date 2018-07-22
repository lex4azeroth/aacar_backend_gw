package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.model.Pricing;

public interface OrderPageService {

	List<OrderSummaryModel> myOrderSummaryList(String id, int size);
	
	OrderDetailModel myOrderDetail(int orderId, String validId);
	
	OrderDetailWithWasherModel orderDetailWithWasher(int orderId);
	
	List<OrderDetailWithWasherModel> listAllOrderDetails();
	
	Pricing pricing(String validId, int orderId, int couponId, int promotionId);
	
	boolean deal(int orderId, double discountedPrice, int promotionId, int couponId);
}
