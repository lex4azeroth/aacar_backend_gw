package com.aawashcar.apigateway.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Location;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.model.DistrictOnlyModel;
import com.aawashcar.apigateway.model.LatestOrder;
import com.aawashcar.apigateway.model.LocationModel;
import com.aawashcar.apigateway.model.MainPageInfo;
import com.aawashcar.apigateway.model.OrderModel;
import com.aawashcar.apigateway.model.ResidentialQuarterModel;
import com.aawashcar.apigateway.model.Store;
import com.aawashcar.apigateway.service.MainPageInfoService;
import com.aawashcar.apigateway.util.EntityMapper;
import com.aawashcar.apigateway.util.ServiceUtil;

@Service()
public class MainPageInfoServiceImpl extends BaseService implements MainPageInfoService {

	@Override
	public MainPageInfo getMainPageInfo(String validId) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, validId);
		User user = restTemplate.getForObject(url, User.class);

		VehicleCategory[] vehicleCategories = null;
		VehicleType[] vehicleTypes = null;
		// WashCarService[] services = null;
		// City city = null;
		// District district = null;
		// Province province = null;
		// ResidentialQuarter resiQuarter = null;

		url = opsUrlPrefix + "vehicle/categories";
		ResponseEntity<VehicleCategory[]> vehicleCategoryResponseEntity = restTemplate.getForEntity(url,
				VehicleCategory[].class);
		vehicleCategories = vehicleCategoryResponseEntity.getBody();

		url = opsUrlPrefix + "vehicle/types";
		ResponseEntity<VehicleType[]> vehicleTypeResponseEntity = restTemplate.getForEntity(url, VehicleType[].class);
		vehicleTypes = vehicleTypeResponseEntity.getBody();

		// url = opsUrlPrefix + "wasshcarservice/services";
		// ResponseEntity<WashCarService[]> washCareServiceResponseEntity =
		// restTemplate.getForEntity(url,
		// WashCarService[].class);
		// services = (WashCarService[])
		// washCareServiceResponseEntity.getBody();
		MainPageInfo mainPageInfo = null;
		if (user.getId() > 0) {
			// get latest order by user id;
			url = omsUrlPrefix + "order/latest/" + String.valueOf(user.getId());
			Order order = restTemplate.getForObject(url, Order.class);
			
			LatestOrder latestOrder = new LatestOrder();
			latestOrder.setId(order.getId());
			latestOrder.setOrderNumber(order.getOrderNumber());
			latestOrder.setPrice(order.getPrice());
			latestOrder.setServiceId(order.getServiceId());
			latestOrder.setStoreId(order.getStoreId());
			latestOrder.setCapType(order.getCapabilityType());

			// url = opsUrlPrefix + "location/province/" +
			// String.valueOf(order.getProvinceId());
			// province = restTemplate.getForObject(url, Province.class);
			//
			// url = opsUrlPrefix + "location/city/" +
			// String.valueOf(order.getCityId());
			// city = restTemplate.getForObject(url, City.class);
			//
			// url = opsUrlPrefix + "location/district/" +
			// String.valueOf(order.getDistrictId());
			// district = restTemplate.getForObject(url, District.class);
			//
			// url = opsUrlPrefix + "location/resiquarter/" +
			// String.valueOf(order.getResiQuartId());
			// resiQuarter = restTemplate.getForObject(url,
			// ResidentialQuarter.class);

			url = opsUrlPrefix + "vehicle/" + String.valueOf(order.getVehicleId());
			Vehicle vehicle = restTemplate.getForObject(url, Vehicle.class);

			url = lbsUrlPrefix + "getLocationById/" + String.valueOf(order.getLocationId());
			LocationModel locationModel = restTemplate.getForObject(url, LocationModel.class);

			mainPageInfo = EntityMapper.buildMainPageInfo(user, order, vehicleCategories, vehicleTypes, locationModel,
					vehicle);
			
			mainPageInfo.setLatestOrder(latestOrder);

			// mainPageInfo = EntityMapper.buildMainPageInfo(user, order,
			// vehicleCategories, vehicleTypes, services, city,
			// district, province, resiQuarter, vehicle);

		} else {
			// not found, build default page info
			// url = opsUrlPrefix + "location/city/default";
			// city = restTemplate.getForObject(url, City.class);
			// url = opsUrlPrefix + "location/district/default";
			// district = restTemplate.getForObject(url, District.class);
			// url = opsUrlPrefix + "location/province/default";
			// province = restTemplate.getForObject(url, Province.class);
			// url = opsUrlPrefix + "location/resiquarter/default";
			// resiQuarter = restTemplate.getForObject(url,
			// ResidentialQuarter.class);
			// mainPageInfo =
			// EntityMapper.buildDefaultMainPageInfo(vehicleCategories,
			// vehicleTypes, services, city,
			// district, province, resiQuarter);

			mainPageInfo = EntityMapper.buildDefaultMainPageInfo(vehicleCategories, vehicleTypes, new LocationModel());
			mainPageInfo.setLatestOrder(new LatestOrder());
		}

		return mainPageInfo;
	}

	@Deprecated
	@Override
	public List<DistrictOnlyModel> listDistrictsOnly(int provinceId, int cityId) {
		String url = opsUrlPrefix + "location/district/listall/" + String.valueOf(provinceId) + "/"
				+ String.valueOf(cityId);
		ResponseEntity<District[]> districtResponseEntity = restTemplate.getForEntity(url, District[].class);
		District[] districts = districtResponseEntity.getBody();
		int size = districts.length;
		List<DistrictOnlyModel> districtModels = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			districtModels.add(EntityMapper.convertDistrictOnlyToModel(districts[index]));
		}

		return districtModels;
	}

	@Deprecated
	@Override
	public List<ResidentialQuarterModel> listResidentialQuarters(int provinceId, int cityId, int districtId) {
		String url = opsUrlPrefix + "location/residentialquarter/listall/" + String.valueOf(provinceId) + "/"
				+ String.valueOf(cityId) + "/" + String.valueOf(districtId);
		ResponseEntity<ResidentialQuarter[]> residentialQuarterResponseEntity = restTemplate.getForEntity(url,
				ResidentialQuarter[].class);
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
		String url = opsUrlPrefix + "pricing/price/" + String.valueOf(typeId) + "/" + String.valueOf(categoryId) + "/"
				+ String.valueOf(serviceId);
		return restTemplate.getForObject(url, double.class);
	}

	@Override
	public int newOrder(OrderModel orderModel) {
		// 1. get user id or create new user url:
		// user/uuid-test-2049/12312344423
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, orderModel.getValidId());
		User user = restTemplate.getForObject(url, User.class);
		int userId = user.getId();

		if (userId <= 0) {
			url = crmUrlPrefix + "user/" + orderModel.getValidId() + "/PHONE_PLACE_HOLDER";
			userId = Integer.valueOf(restTemplate.postForObject(url, null, Integer.class));
		}

		// if (userId <= 0) {
		// url = crmUrlPrefix + "user/" + orderModel.getValidId() + "/" +
		// orderModel.getPhoneNumber();
		// userId = Integer.valueOf(restTemplate.postForObject(url, null,
		// Integer.class));
		// } else {
		// if (!user.getPhoneNumber().equals(orderModel.getPhoneNumber())) {
		// // user updated his phone number when making order
		// // update user phone number in crm_user {userid}/{phonenumber}
		// url = crmUrlPrefix + "user/updatephonenumber/" +
		// String.valueOf(userId) + "/"
		// + orderModel.getPhoneNumber();
		// int result = restTemplate.exchange(url, HttpMethod.PUT, null,
		// Integer.class).getBody().intValue();
		// if (result != 1) {
		// // log error here
		// }
		// }
		// }

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

		String[] serviceIds = ServiceUtil.getServiceIDs(orderModel.getServiceId());
		int length = serviceIds.length;
		// double totalPrice = 0d;
		// for (int index = 0; index < length; index++) {
		// totalPrice += getPrice(orderModel.getVehicleType(),
		// orderModel.getVehicleCategory(), Integer.valueOf(serviceIds[index]));
		// }
		//
		// order.setPrice(totalPrice);
		// order.setDiscountedPrice(totalPrice);
		order.setPrice(orderModel.getPrice());
		order.setDiscountedPrice(orderModel.getPrice());

		// // 3. add origin price
		// url = opsUrlPrefix + "wasshcarservice/service/originprice/" +
		// String.valueOf(vehicle.getTypeId()) + "/"
		// + String.valueOf(vehicle.getCategoryId()) + "/" +
		// String.valueOf(orderModel.getServiceId());
		// Double originPrice = restTemplate.getForObject(url, Double.class);
		// order.setPrice(originPrice.doubleValue());
		// order.setDiscountedPrice(originPrice);

		// 4. add location in lbs service
		url = lbsUrlPrefix + "add";
		Location location = new Location();
		location.setAddressRemark(orderModel.getAddressRemark());
		location.setDetailAddress(orderModel.getDetailLocation());
		location.setLatitude(orderModel.getLatitude());
		location.setLongitude(orderModel.getLongitude());
		location.setOpenId(orderModel.getValidId());
		location.setUserId(user.getId());
		HttpEntity<Location> postLocation = new HttpEntity<Location>(location, headers);
		int locationId = restTemplate.postForObject(url, postLocation, Integer.class);
		order.setLocationId(locationId);
		order.setServiceId(orderModel.getServiceId());

		url = omsUrlPrefix + "order/new";
		HttpEntity<Order> postEntity = new HttpEntity<Order>(order, headers);
		Integer orderId = restTemplate.postForObject(url, postEntity, Integer.class);

		return orderId.intValue();
	}

	@Override
	public List<Store> listStores() {
		String url = opsUrlPrefix + "store/listall";
		ResponseEntity<Store[]> storeResponseEntity = restTemplate.getForEntity(url, Store[].class);
		Store[] stores = storeResponseEntity.getBody();
		ArrayList<Store> storesList = new ArrayList();
		for (Store store : stores) {
			storesList.add(store);
		}
		return storesList;
	}
}
