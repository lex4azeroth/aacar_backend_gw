package com.aawashcar.apigateway.service;

import com.aawashcar.apigateway.entity.WorkerRemark;
import com.aawashcar.apigateway.model.WasherActionModel;
import com.aawashcar.apigateway.model.WasherActionResponse;
import com.aawashcar.apigateway.model.WasherMainPageInfo;

public interface WasherPageService {
	
	WasherMainPageInfo login(String validId);
	
	WasherActionResponse takeOrder(WasherActionModel model);
	
	WasherActionResponse rejectOrder(WasherActionModel model);
	
	WasherActionResponse completeOrder(WasherActionModel model);
	
	WorkerRemark[] listRemarks();

}
