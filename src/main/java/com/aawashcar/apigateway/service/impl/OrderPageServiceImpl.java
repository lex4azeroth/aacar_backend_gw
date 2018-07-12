package com.aawashcar.apigateway.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aawashcar.apigateway.entity.City;
import com.aawashcar.apigateway.entity.Coupon;
import com.aawashcar.apigateway.entity.District;
import com.aawashcar.apigateway.entity.Order;
import com.aawashcar.apigateway.entity.OrderSummary;
import com.aawashcar.apigateway.entity.Promotion;
import com.aawashcar.apigateway.entity.Province;
import com.aawashcar.apigateway.entity.ResidentialQuarter;
import com.aawashcar.apigateway.entity.User;
import com.aawashcar.apigateway.entity.Vehicle;
import com.aawashcar.apigateway.entity.VehicleCategory;
import com.aawashcar.apigateway.entity.VehicleType;
import com.aawashcar.apigateway.entity.WashCarService;
import com.aawashcar.apigateway.entity.Worker;
import com.aawashcar.apigateway.model.OrderDetailModel;
import com.aawashcar.apigateway.model.OrderDetailWithWasherModel;
import com.aawashcar.apigateway.model.OrderSummaryModel;
import com.aawashcar.apigateway.service.OrderPageService;
import com.aawashcar.apigateway.util.EntityMapper;

@Service()
public class OrderPageServiceImpl extends BaseService implements OrderPageService {

	@Override
	public List<OrderSummaryModel> myOrderSummaryList(String validId, int limit) {
		User user = getUserId(validId);
		int userId = user.getId();
		if (userId > 0) {
			String url = omsUrlPrefix + "order/myordersummaries/" + String.valueOf(userId) + "/" +
			             String.valueOf(limit);
			ResponseEntity<OrderSummary[]> orderSummaryResponseEntity =
			                restTemplate.getForEntity(url, OrderSummary[].class);
			OrderSummary[] myOrderSummarys = orderSummaryResponseEntity.getBody();
			List<OrderSummaryModel> orderSummaryModels =
			                EntityMapper.convertOrderSummarysToMyModel(myOrderSummarys);

			int size = orderSummaryModels.size();
			for (int index = 0; index < size; index++) {
				int serviceId = myOrderSummarys[index].getServiceId();
				url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(serviceId);
				WashCarService washCarService = restTemplate.getForObject(url, WashCarService.class);
				orderSummaryModels.get(index).setServiceName(washCarService.getName());
			}

			return orderSummaryModels;
		}
		else {
			throw new RuntimeException(String.format("Id %s does not exist", validId));
		}
	}

	@Override
	public OrderDetailModel myOrderDetail(int orderId, String validId) {
		User user = getUserId(validId);

		if (user == null) {
			throw new RuntimeException("user not found");
		}

		City city = null;
		District district = null;
		Province province = null;
		ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
		WashCarService washCarService = null;

		if (user.getId() > 0) {

			// get order by order id;
			String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
			Order order = restTemplate.getForObject(url, Order.class);

			url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(order.getServiceId());
			washCarService = restTemplate.getForObject(url, WashCarService.class);

			url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
			vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

			url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
			vehicleType = restTemplate.getForObject(url, VehicleType.class);

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

			url = promUrlPrefix + "coupon/mylist/" + String.valueOf(user.getId());
			ResponseEntity<Coupon[]> couponResponseEntity = restTemplate.getForEntity(url, Coupon[].class);
			Coupon[] myCoupons = couponResponseEntity.getBody();

			url = promUrlPrefix + "promotion/mylist/" + String.valueOf(user.getId());
			ResponseEntity<Promotion[]> promotionResponseEntity = restTemplate.getForEntity(url, Promotion[].class);
			Promotion[] myPromotions = promotionResponseEntity.getBody();

			return EntityMapper.buildOrderDetailInfo(
			                                         order,
			                                         washCarService.getName(),
			                                         vehicle.getColor(),
			                                         vehicleCategory,
			                                         vehicleType,
			                                         province,
			                                         city,
			                                         district,
			                                         resiQuarter,
			                                         myCoupons,
			                                         myPromotions);
		}

		return null;
	}

	private User getUserId(String validId) {
		String url = "%s/user/%s";

		url = String.format(url, crmUrlPrefix, validId);
		User user = restTemplate.getForObject(url, User.class);

		return user;
	}

	@Override
	public OrderDetailWithWasherModel orderDetailWithWasher(int orderId) {
		City city = null;
		District district = null;
		Province province = null;
		ResidentialQuarter resiQuarter = null;
		VehicleCategory vehicleCategory = null;
		VehicleType vehicleType = null;
		WashCarService washCarService = null;

		String url = omsUrlPrefix + "order/detail/" + String.valueOf(orderId);
		Order order = restTemplate.getForObject(url, Order.class);

		url = opsUrlPrefix + "wasshcarservice/service/" + String.valueOf(order.getServiceId());
		washCarService = restTemplate.getForObject(url, WashCarService.class);

		url = opsUrlPrefix + "vehicle/vehiclecategory/" + String.valueOf(order.getVehicleId());
		vehicleCategory = restTemplate.getForObject(url, VehicleCategory.class);

		url = opsUrlPrefix + "vehicle/vehicletype/" + String.valueOf(order.getVehicleId());
		vehicleType = restTemplate.getForObject(url, VehicleType.class);

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

		url = promUrlPrefix + "coupon/" + String.valueOf(order.getCouponId());
		Coupon coupon = restTemplate.getForObject(url, Coupon.class);
		Coupon[] coupons = {coupon};

		url = promUrlPrefix + "promotion/" + String.valueOf(order.getPromotionId());
		Promotion promotion = restTemplate.getForObject(url, Promotion.class);
		Promotion[] promotions = {promotion};
		url = opsUrlPrefix + "worker/washedorder/" + String.valueOf(order.getId());
		Worker worker = restTemplate.getForObject(url, Worker.class);
		return EntityMapper.buildOrderDetailWithWasher(
		                                               order,
		                                               washCarService.getName(),
		                                               vehicle.getColor(),
		                                               vehicleCategory,
		                                               vehicleType,
		                                               province,
		                                               city,
		                                               district,
		                                               resiQuarter,
		                                               coupons,
		                                               promotions,
		                                               worker);
	}
}
