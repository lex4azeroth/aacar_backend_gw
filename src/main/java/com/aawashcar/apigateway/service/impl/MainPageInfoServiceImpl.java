package com.aawashcar.apigateway.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.model.DistrictModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;
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
		MainPageInfo mainPageInfo = null;
		if (user.getId() > 0) {
			// get latest order by user id;
			url = omsUrlPrefix + "order/latest/" + String.valueOf(user.getId());
			Order order = restTemplate.getForObject(url, Order.class);
			
			url = opsUrlPrefix + "location/province/" + String.valueOf(order.getProvinceId());
			province = restTemplate.getForObject(url, Province.class);
			
			url = opsUrlPrefix + "location/city/" + String.valueOf(order.getCityId());
			city = restTemplate.getForObject(url, City.class);
			
			url = opsUrlPrefix + "location/district/" + String.valueOf(order.getDistrictId());
			district = restTemplate.getForObject(url, District.class);
			
			url = opsUrlPrefix + "location/resiquarter/" + String.valueOf(order.getResiQuartId());
			resiQuarter = restTemplate.getForObject(url, ResidentialQuarter.class);
			
			url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
			Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);
			mainPageInfo = EntityMapper.buildMainPageInfo(
			                                              user, order, vehicleCategories, vehicleTypes, services, 
			                                              city, district, province, resiQuarter, vehicle);
			
		} else {
			// not found, build default page info
			url = opsUrlPrefix + "location/city/default";
			city = restTemplate.getForObject(url, City.class);
			url = opsUrlPrefix + "location/district/default";
			district = restTemplate.getForObject(url, District.class);
			url = opsUrlPrefix + "location/province/default";
			province = restTemplate.getForObject(url, Province.class);
			url = opsUrlPrefix + "location/resiquarter/default";
			resiQuarter = restTemplate.getForObject(url, ResidentialQuarter.class);
			mainPageInfo = EntityMapper.buildDefaultMainPageInfo(vehicleCategories, vehicleTypes, services, city, district, province, resiQuarter);
		}
		
		return mainPageInfo;
	}

	@Override
	public List<DistrictModel> listDistricts(int provinceId, int cityId) {
		String url = opsUrlPrefix + "location/district/listall/" + String.valueOf(provinceId) + "/" + String.valueOf(cityId);
		ResponseEntity<District[]> districtResponseEntity = restTemplate.getForEntity(url, District[].class);
		District[] districts = districtResponseEntity.getBody();
		int size = districts.length;
		List<DistrictModel> districtModels = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			districtModels.add(EntityMapper.convertDistrictToModel(districts[index]));
		}
		
		return districtModels;
	}

	@Override
	public List<ResidentialQuarterModel> listResidentialQuarters(int provinceId, int cityId, int districtId) {
		String url = opsUrlPrefix + "location/residentialquarter/listall/" + String.valueOf(provinceId) + "/" + String.valueOf(cityId) + "/" + String.valueOf(districtId);
		ResponseEntity<ResidentialQuarter[]> residentialQuarterResponseEntity = restTemplate.getForEntity(url, ResidentialQuarter[].class);
		ResidentialQuarter[] residentialQuarters = residentialQuarterResponseEntity.getBody();
		int size = residentialQuarters.length;
		List<ResidentialQuarterModel> residentialQuarterModels = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			residentialQuarterModels.add(EntityMapper.convertResidentialQuarterToModel(residentialQuarters[index]));
		}
		
		return residentialQuarterModels;
	}

	@Override
	public double getPrice(int typeId, int categoryId, int serviceId) {
		String url = opsUrlPrefix + "pricing/price/" + String.valueOf(typeId) + "/" + String.valueOf(categoryId) + "/" + String.valueOf(serviceId);
		return restTemplate.getForObject(url, double.class);
	}

	@Override
	public int newOrder(OrderModel orderModel) {
		// 1. get user id or create new user url: user/uuid-test-2049/12312344423
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, orderModel.getUuid());
		User user = restTemplate.getForObject(url, User.class);
		int userId = user.getId();
		
		if (userId <= 0) {
			url = crmUrlPrefix + "user/" + orderModel.getUuid() + "/" + orderModel.getPhoneNumber();
			userId = Integer.valueOf(restTemplate.postForObject(url, null, Integer.class));	
		} else {
			// double check the phoneNumber in User and OrderModel are identical
			if (!user.getPhoneNumber().equals(orderModel.getPhoneNumber())) {
				throw new RuntimeException();
			}
		}
		
		// 2. add order
		Order order = new Order();
		order.setUserId(userId);
		
		EntityMapper.convertOrderModelToOrder(orderModel, order);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        
		String license = orderModel.getLicense();
		url = opsUrlPrefix + "vehicle/license/" + license;
		Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);
		if (vehicle == null) {
			vehicle = new Vehicle();
			vehicle.setLicense(license);
			vehicle.setCategoryId(orderModel.getVehicleCategory());
			vehicle.setColor(orderModel.getColor());
			vehicle.setTypeId(orderModel.getVehicleType());
			url = opsUrlPrefix + "vehicle/new";
			HttpEntity<Vehicle> postVehicle = new HttpEntity<Vehicle>(vehicle, headers);
			int vehicleId = restTemplate.postForObject(url, postVehicle, Integer.class);
			
			order.setVehicleId(vehicleId);
		} else {
			order.setVehicleId(vehicle.getId());
		}
		
		url = omsUrlPrefix + "order/new";
//		HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Order> postEntity = new HttpEntity<Order>(order, headers);
		Integer orderId = restTemplate.postForObject(url, postEntity, Integer.class);
		
		return orderId.intValue();
	}
}
