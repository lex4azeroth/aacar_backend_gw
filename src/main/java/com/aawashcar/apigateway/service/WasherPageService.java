package com.aawashcar.apigateway.service;

import com.aawashcar.apigateway.entity.WasherOrderSummary;
import com.aawashcar.apigateway.entity.WorkerRemark;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.WasherActionModel;
import com.aawashcar.apigateway.model.WasherActionResponse;
import com.aawashcar.apigateway.model.WasherMainPageInfo;
import com.aawashcar.apigateway.model.WasherOrderSummaryModel;

public interface WasherPageService {
	
	WasherMainPageInfo login(String validId);
	
	WasherActionResponse takeOrder(WasherActionModel model);
	
	WasherActionResponse rushOrder(WasherActionModel model);
	
	WasherActionResponse rejectOrder(WasherActionModel model);
	
	WasherActionResponse arrivedOrder(WasherActionModel model);
	
	WasherActionResponse completeOrder(WasherActionModel model);
	
	WorkerRemark[] listRemarks();
	
	WorkerRemark[] listAccpetRemarks();
	
	WorkerRemark[] listRejectRemarks();
	
	WorkerRemark[] listCompleteRemarks();
	
	OrderDetailModel orderDetail(int orderId);
	
	WasherOrderSummaryModel[] listWasherCompletedOrderSummary(String validId, int size);
	
	WasherOrderSummaryModel[] listWasherUnCompletedOrderSummary(String validId, int size);
	
	WasherOrderSummaryModel[] listWasherAvailableOrderSummary(String validId, int size);
	
	String apply(String validId, String phoneNumber);
}
