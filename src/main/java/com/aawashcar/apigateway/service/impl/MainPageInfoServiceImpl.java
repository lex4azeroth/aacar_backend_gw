package com.aawashcar.apigateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.service.MainPageInfoService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class MainPageInfoServiceImpl implements MainPageInfoService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${mcw.service.crm.url.prefix}")
	private String crmUrlPrefix;
	
	@Value("${mcw.service.oms.url.prefix}")
	private String omsUrlPrefix;
	
	@Value("${mcw.service.ops.url.prefix}")
	private String opsUrlPrefix;
	
//	@Value("${mcw.service.prom.url.prefix}")
//	private String promUrlPrefix;

	@Override
	public MainPageInfo getMainPageInfo(String uuid) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, uuid);
		User user = restTemplate.getForObject(url, User.class);
		
		VehicleCategory[] vehicleCategories = null;
		VehicleType[] vehicleTypes = null;
		WashCarService[] services = null;
		City city = null;
		District district = null;
		Province province = null;
		ResidentialQuarter resiQuarter = null;
		
		url = opsUrlPrefix + "vehicle/categories";
		ResponseEntity<VehicleCategory[]> vehicleCategoryResponseEntity = restTemplate.getForEntity(url, VehicleCategory[].class);
		vehicleCategories =  vehicleCategoryResponseEntity.getBody();
		
		url = opsUrlPrefix + "vehicle/types";
		ResponseEntity<VehicleType[]> vehicleTypeResponseEntity = restTemplate.getForEntity(url, VehicleType[].class);
		vehicleTypes = vehicleTypeResponseEntity.getBody();
		
		url = opsUrlPrefix + "wasshcarservice/services";
		ResponseEntity<WashCarService[]> washCareServiceResponseEntity = restTemplate.getForEntity(url, WashCarService[].class);
		services = (WashCarService[]) washCareServiceResponseEntity.getBody();
		
		if (user.getId() > 0) {
			// get latest order by user id;
		} else {
			// not found, build default page info
//			List.class categories = restTemplate.getForObject(url, List.class);
			url = opsUrlPrefix + "location/province/default";
			city = restTemplate.getForObject(url, City.class);
			district = restTemplate.getForObject(url, District.class);
			province = restTemplate.getForObject(url, Province.class);
			resiQuarter = restTemplate.getForObject(url, ResidentialQuarter.class);

		}
		
		return EntityMapper.buildDefaultMainPageInfo(vehicleCategories, vehicleTypes, services, city, district, province, resiQuarter);
	}

}
