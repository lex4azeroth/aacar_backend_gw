package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.DistrictOnlyModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;
import com.aawashcar.apigateway.model.Store;

public interface MainPageInfoService {

	MainPageInfo getMainPageInfo(String validId);

	@Deprecated
	List<DistrictOnlyModel> listDistrictsOnly(int provinceId, int cityId);

	@Deprecated
	List<ResidentialQuarterModel> listResidentialQuarters(int provinceId, int cityId, int districtId);

	double getPrice(int typeId, int categoryId, int serviceId);

	int newOrder(OrderModel orderModel);
	
	List<Store> listStores();
}
