package com.aawashcar.apigateway.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.model.CapabilityModel;
import com.aawashcar.apigateway.model.CapabilityTypeModel;
import com.aawashcar.apigateway.service.CapabilityPageService;

@Service
public class CapabilityPageServiceImpl extends BaseService implements CapabilityPageService {

	@Override
	public List<CapabilityModel> listAllCapabilities() {
		String url = capUrlPrefix + "capabilities";

		ResponseEntity<CapabilityModel[]> capabilityResponseEntity = restTemplate.getForEntity(url, CapabilityModel[].class);
		CapabilityModel[] capabilities = capabilityResponseEntity.getBody();
		List<CapabilityModel> result = new ArrayList<CapabilityModel>(capabilities.length);
		Collections.addAll(result, capabilities);
		return result;
	}
	
	@Override
	public CapabilityModel findCapabilityById(int id) {
		String url = capUrlPrefix + "capability/" + String.valueOf(id);

		ResponseEntity<CapabilityModel> capabilityResponseEntity = restTemplate.getForEntity(url, CapabilityModel.class);
		CapabilityModel capability = capabilityResponseEntity.getBody();
		return capability;
	}

	@Override
	public List<CapabilityTypeModel> listCapabilityTypes() {
		String url = capUrlPrefix + "capabilityTypes";
		ResponseEntity<CapabilityTypeModel[]> capabilityTypeResponseEntity = restTemplate.getForEntity(url, CapabilityTypeModel[].class);
		CapabilityTypeModel[] capabilityTypes = capabilityTypeResponseEntity.getBody();
		List<CapabilityTypeModel> result = new ArrayList<CapabilityTypeModel>(capabilityTypes.length);
		Collections.addAll(result, capabilityTypes);
		return result;
	}

	@Override
	public List<CapabilityModel> listAllWithPrice(int vehicleTypeId, int vehicleCategoryId) {
		List<CapabilityModel> capabilities = listAllCapabilities();
		
		capabilities.forEach(capability -> {
			capability.setPrice(getPrice(vehicleTypeId, vehicleCategoryId, capability.getId()));
		});
		
		return capabilities;
	}
	
	public double getPrice(int typeId, int categoryId, int serviceId) {
		String url = opsUrlPrefix + "pricing/price/" + String.valueOf(typeId) + "/" + String.valueOf(categoryId) + "/"
				+ String.valueOf(serviceId);
		return restTemplate.getForObject(url, double.class);
	}
}
