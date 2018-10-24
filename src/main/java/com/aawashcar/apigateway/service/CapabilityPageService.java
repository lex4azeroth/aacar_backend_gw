package com.aawashcar.apigateway.service;

import java.util.List;

import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.CapabilityTypeModel;

public interface CapabilityPageService {
	
	List<CapabilityModel> listAllCapabilities();

	List<CapabilityModel> listAllWithPrice(int vehicleTypeId, int vehicleCategoryId);
	
	List<CapabilityTypeModel> listCapabilityTypes();
	
	CapabilityModel findCapabilityById(int id);

}
