package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.DistrictModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;

public interface MainPageInfoService {
	
	MainPageInfo getMainPageInfo(String uuid);
	
	List<DistrictModel> listDistricts(int provinceId, int cityId);
	
	List<ResidentialQuarterModel> listResidentialQuarters(int provinceId, int cityId, int districtId);
	
	double getPrice(int typeId, int categoryId, int serviceId);
	
	int newOrder(OrderModel orderModel);
}
