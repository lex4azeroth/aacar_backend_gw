package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.OrderSummaryModel;

public interface OrderPageService {

	List<OrderSummaryModel> myOrderSummaryList(String uuid, int size);
}
