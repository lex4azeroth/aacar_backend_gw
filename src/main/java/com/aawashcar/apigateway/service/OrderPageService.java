package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;

public interface OrderPageService {

	List<OrderSummaryModel> myOrderSummaryList(String id, int size);
	
	OrderDetailModel myOrderDetail(int orderId, String validId);
	
	OrderDetailWithWasherModel orderDetailWithWasher(int orderId);
}
